package com.example.li_auto_shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
public class DashboardController {
    @FXML
    private static Label testText;
    @FXML
    public static void WelcomeName(String name) {
        testText.setText("Welcome, " + name);
    }
    @FXML
    private void LogOut() {
        Utils.ChangeScene("signup-login.fxml");
        testText.getScene().getWindow().hide();
//        new DashboardController().WelcomeName("This is a test");
        WelcomeName("This is a test");
    }
    // region Window Settings
    @FXML
    private void Minimize(ActionEvent event) {
        Utils.Minimize(event);
    }
    
    @FXML
    private void Close() {
        Utils.Close();
    }
    
    // endregion
}