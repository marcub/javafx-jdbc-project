package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load());
            stage.setScene(mainScene);
            stage.setTitle("Sample JavaFX application");
            stage.show();
        }
        catch(Exception e) {
            e.getStackTrace();
        }
    }
}
