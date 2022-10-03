package gui;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {


    private DepartmentService service;

    private ObservableList<Department> observableList;

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

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
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

    public void updateTableViewData() {
        if (service == null) {
            throw new IllegalStateException("Service was null!");
        }
        List<Department> allDepartments = service.findAll();
        observableList = FXCollections.observableArrayList(allDepartments);
        tableViewDepartmentList.setItems(observableList);

    }
}
