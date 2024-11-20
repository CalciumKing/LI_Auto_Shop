package com.example.li_auto_shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Utils {
    private static double xOffset;
    private static double yOffset;
    private static final String imageFolderPath = System.getProperty("user.dir") + "\\bin\\Images";
    public static void errorAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public static void changeScene(String sceneName) {
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
            errorAlert(Alert.AlertType.ERROR, "Scene Error", "Error Changing Scene", "There was an error changing scenes, please try again");
        }
    }
    public static void createImage(String path, ImageView imageView) {
        File imageDir = new File(imageFolderPath);
        File imageFile;
        imageFile = new File(imageDir, ((path == null) ? "fileNotFound.png" : path));
        Image image = new Image(imageFile.toURI().toString(), 125, 165, true, true);
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
