package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("onMenuItemSellerAction");
    }
    @FXML
    public void onMenuItemDepartmentAction() {
        loadViewTemp("/gui/DepartmentList.fxml");
    }
    @FXML
    public void onMenuItemAboutAction() {
        loadView("/gui/About.fxml");
    }

    private synchronized void loadView (String viewAbsoluteName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewAbsoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());
        }
        catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view!", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //LoadView temporária para teste
    private synchronized void loadViewTemp (String viewAbsoluteName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewAbsoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            DepartmentListController controller = loader.getController();
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableViewData();
        }
        catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view!", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
