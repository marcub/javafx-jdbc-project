package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department departmentEntity;
    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    @FXML
    private Button buttonSaveDepartment;
    @FXML
    private Button buttonCancelDepartment;
    @FXML
    private TextField textFieldDepartmentId;
    @FXML
    private TextField textFieldDepartmentName;
    @FXML
    private Label labelErrorName;

    public void setDepartment(Department departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    public void setService(DepartmentService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        this.dataChangeListeners.add(listener);
    }

    @FXML
    public void onButtonSaveDepartmentAction(ActionEvent event) {
        if (departmentEntity == null) {
            throw new IllegalStateException("Entity null!");
        }
        if (service == null) {
            throw new IllegalStateException("Service null!");
        }
        try {
            departmentEntity = getFormData();
            service.saveOrUpdate(departmentEntity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving department", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department department = new Department();

        ValidationException exception = new ValidationException("Validation Error");

        department.setId(Utils.tryParseToInt(textFieldDepartmentId.getText()));

        if (textFieldDepartmentName.getText() == null || textFieldDepartmentName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }
        department.setName(textFieldDepartmentName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return department;
    }

    @FXML
    public void onButtonCancelDepartmentAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textFieldDepartmentId);
        Constraints.setTextFieldMaxLength(textFieldDepartmentName, 40);
    }

    public void updateFormData() {
        if (departmentEntity == null) {
            throw new IllegalStateException("Entity was null!");
        }
        textFieldDepartmentId.setText(String.valueOf(departmentEntity.getId()));
        textFieldDepartmentName.setText(departmentEntity.getName());
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}
