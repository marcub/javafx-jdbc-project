package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department departmentEntity;
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

    @FXML
    public void onButtonSaveDepartmentAction() {
        System.out.println("Save");
    }

    @FXML
    public void onButtonCancelDepartmentAction() {
        System.out.println("Cancel");
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
