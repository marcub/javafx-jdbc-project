package gui;

import db.DbException;
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
import model.services.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department departmentEntity;
    private DepartmentService service;
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
            Utils.currentStage(event).close();
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving department", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private Department getFormData() {
        Department department = new Department();
        department.setId(Utils.tryParseToInt(textFieldDepartmentId.getText()));
        department.setName(textFieldDepartmentName.getText());
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
}
