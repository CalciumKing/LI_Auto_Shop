package com.example.li_auto_shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Utils {
    private static double xOffset;
    private static double yOffset;
    public void ErrorAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public void ChangeScene(String sceneName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(sceneName));
//            DashboardController dashboardController = fxmlLoader.getController();
//            dashboardController.WelcomeName("Landen");
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Automotive Application");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception ignored) {
            new Utils().ErrorAlert(Alert.AlertType.ERROR, "Scene Error", "Error Changing Scene", "There was an error changing scenes, please try again");
        }
    }
    // region Window Settings
    public void Minimize(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }
    public void Close() {
        System.exit(0);
    }
    public void WindowClick(MouseEvent event) {
        xOffset = event.getScreenX();
        yOffset = event.getScreenY();
    }
    public void WindowDrag(MouseEvent event, AnchorPane pane) {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    // endregion
}
