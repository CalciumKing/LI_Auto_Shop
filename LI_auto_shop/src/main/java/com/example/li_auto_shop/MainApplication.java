package com.example.li_auto_shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("signup-login.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Automotive Application");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
}