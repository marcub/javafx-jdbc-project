package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller sellerEntity;
    private SellerService service;
    private DepartmentService departmentService;
    private ObservableList<Department> observableListDepartment;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    @FXML
    private Button buttonSaveSeller;
    @FXML
    private Button buttonCancelSeller;
    @FXML
    private TextField textFieldSellerId;
    @FXML
    private TextField textFieldSellerName;
    @FXML
    private TextField textFieldSellerEmail;
    @FXML
    private DatePicker datePickerBirthDate;
    @FXML
    private TextField textFieldSellerBaseSalary;
    @FXML
    private ComboBox<Department> comboBoxDepartment;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;

    public void setSeller(Seller sellerEntity) {
        this.sellerEntity = sellerEntity;
    }

    public void setServices(SellerService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        this.dataChangeListeners.add(listener);
    }

    @FXML
    public void onButtonSaveSellerAction(ActionEvent event) {
        if (sellerEntity == null) {
            throw new IllegalStateException("Entity null!");
        }
        if (service == null) {
            throw new IllegalStateException("Service null!");
        }
        try {
            sellerEntity = getFormData();
            service.saveOrUpdate(sellerEntity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving seller", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller seller = new Seller();

        ValidationException exception = new ValidationException("Validation Error");

        seller.setId(Utils.tryParseToInt(textFieldSellerId.getText()));

        if (textFieldSellerName.getText() == null || textFieldSellerName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }
        seller.setName(textFieldSellerName.getText());

        if (textFieldSellerEmail.getText() == null || textFieldSellerEmail.getText().trim().equals("")) {
            exception.addError("email", "Field can't be empty");
        }
        seller.setEmail(textFieldSellerEmail.getText());

        if (datePickerBirthDate.getValue() == null) {
            exception.addError("birthDate", "Field can't be empty");
        }
        else if (datePickerBirthDate.getValue().isAfter(LocalDate.now(ZoneId.systemDefault()))) {
            exception.addError("birthDate", "BirthDate can't be greater than current date");
        }
        else {
            seller.setBirthDate(Date.from(Instant.from(datePickerBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }


        if (textFieldSellerBaseSalary.getText() == null || textFieldSellerBaseSalary.getText().trim().equals("")) {
            exception.addError("baseSalary", "Field can't be empty");
        }
        seller.setBaseSalary(Utils.tryParseToDouble(textFieldSellerBaseSalary.getText()));

        seller.setDepartment(comboBoxDepartment.getValue());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return seller;
    }

    @FXML
    public void onButtonCancelSellerAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textFieldSellerId);
        Constraints.setTextFieldMaxLength(textFieldSellerName, 70);
        Constraints.setTextFieldMaxLength(textFieldSellerEmail, 100);
        Constraints.setTextFieldDouble(textFieldSellerBaseSalary);
        Utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");

        initializeComboBoxDepartment();
    }

    public void updateFormData() {
        if (sellerEntity == null) {
            throw new IllegalStateException("Entity was null!");
        }
        textFieldSellerId.setText(String.valueOf(sellerEntity.getId()));
        textFieldSellerName.setText(sellerEntity.getName());
        textFieldSellerEmail.setText(sellerEntity.getEmail());
        Locale.setDefault(Locale.US);
        textFieldSellerBaseSalary.setText(String.format("%.2f", sellerEntity.getBaseSalary()));
        if (sellerEntity.getBirthDate() != null) {
            datePickerBirthDate.setValue(LocalDate.ofInstant(sellerEntity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        if(sellerEntity.getDepartment() == null) {
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartment.setValue(sellerEntity.getDepartment());
        }
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("name") ? errors.get("name") : "" ));

        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : "" ));

        labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : "" ));

        labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : "" ));
    }

    public void loadAssociatedObjects() {
        if (departmentService == null) {
            throw new IllegalStateException("DepartmentService was null!");
        }

        List<Department> allDepartments = departmentService.findAll();
        observableListDepartment = FXCollections.observableArrayList(allDepartments);
        comboBoxDepartment.setItems(observableListDepartment);
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty ? "" : department.getName());
            }
        };

        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}
