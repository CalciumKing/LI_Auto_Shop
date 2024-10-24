package com.example.li_auto_shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
public class DashboardController {
    @FXML
    private static Label welcomeText;
    public static void WelcomeName(String name) {
        welcomeText.setText("Welcome, " + name);
    }
    @FXML
    private void LogOut() {
        Utils.ChangeScene("signup-login.fxml");
        ((Stage) welcomeText.getScene().getWindow()).close();
        DashboardController.WelcomeName("NAME HERE");
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