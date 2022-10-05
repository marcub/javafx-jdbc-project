package gui;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import javax.swing.text.SimpleAttributeSet;
import java.io.IOException;
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
    private void onButtonNewDepartment(ActionEvent event) {
        createDialogForm("/gui/DepartmentForm.fxml", Utils.currentStage(event));
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

    public void createDialogForm(String viewAbsoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewAbsoluteName));
            Pane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
