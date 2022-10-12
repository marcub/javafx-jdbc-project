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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller sellerEntity;
    private SellerService service;

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
    private Label labelErrorName;

    public void setSeller(Seller sellerEntity) {
        this.sellerEntity = sellerEntity;
    }

    public void setService(SellerService service) {
        this.service = service;
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
        Constraints.setTextFieldMaxLength(textFieldSellerName, 40);
    }

    public void updateFormData() {
        if (sellerEntity == null) {
            throw new IllegalStateException("Entity was null!");
        }
        textFieldSellerId.setText(String.valueOf(sellerEntity.getId()));
        textFieldSellerName.setText(sellerEntity.getName());
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}
