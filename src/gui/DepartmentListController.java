package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML
    private Button buttonNewDepartment;

    @FXML
    private TableView<Department> tableViewDepartmentList;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private void onButtonNewDepartment() {
        System.out.println("onButtonNewDepartment");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Dica para fazer o TableView acompanhar o tamanho da janela
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartmentList.prefHeightProperty().bind(stage.heightProperty());
    }
}
