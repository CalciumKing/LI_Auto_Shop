package com.example.li_auto_shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;

public class MainApplication extends Application {
    private double xOffset;
    private double yOffset;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Automotive Application");
        stage.setScene(scene);
        stage.show();
        
        
        
        /*scene.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getScreenX();
            yOffset = event.getScreenY();
        });
        scene.setOnMousePressed((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });*/
    }
    
    public static void main(String[] args) {
        launch();
    }
}