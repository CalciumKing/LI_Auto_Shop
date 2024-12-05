package com.example.li_auto_shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Optional;

public class Utils {
    private static double xOffset;
    private static double yOffset;
    
    // region Alert Methods
    public static void errorAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = createAlert(type, title, headerText, contentText);
        alert.showAndWait();
    }
    
    public static Optional<ButtonType> confirmAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = createAlert(type, title, headerText, contentText);
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);
        return alert.showAndWait();
    }
    
    private static Alert createAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }
    // endregion Alert Methods
    
    public static void changeScene(String sceneName, User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(sceneName));
            Parent root = fxmlLoader.load();
            
            if (user != null) {
                DashboardController dashboardController = fxmlLoader.getController();
                dashboardController.welcomeName(user.getUsername());
            }
            
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Automotive Application");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (Exception ignored) {
            errorAlert(Alert.AlertType.ERROR, "Scene Error", "Error Changing Scene", "There was an error changing scenes, please try again");
        }
    }
    
    public static void createImage(String path, ImageView imageView) {
        String uri = (path != null) ? "file:" + path : "file:" + "bin\\Images\\NoImage.jpg";
        Image image = new Image(uri, 125, 165, true, true);
        
        if (image.isError())
            System.out.println("Error loading image: " + image.getException().getMessage());
        else
            imageView.setImage(image);
    }
    
    // region Window Settings
    public static void windowMinimize(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }
    
    public static void windowClose() {
        System.exit(0);
    }
    
    public static void windowClick(MouseEvent event) {
        xOffset = event.getScreenX();
        yOffset = event.getScreenY();
    }
    
    public static void windowDrag(MouseEvent event, AnchorPane pane) {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    // endregion
}