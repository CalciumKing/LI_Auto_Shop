package com.example.li_auto_shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Utils {
    public static void ErrorAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public static void ChangeScene(String sceneName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(sceneName));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
//            DashboardController dbController = fxmlLoader.getController();
//            DashboardController.WelcomeName(new SignupLoginController().GetUsername());
            
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Automotive Application");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ignored) {
            ErrorAlert(Alert.AlertType.ERROR, "Scene Error", "Error Changing Scene", "There was an error changing scenes, please try again");
        }
    }
    // region Window Settings
    public static void Minimize(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }
    
    public static void Close() {
        System.exit(0);
    }
    
    // endregion
}
