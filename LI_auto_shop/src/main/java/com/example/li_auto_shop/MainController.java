package com.example.li_auto_shop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class MainController {
    // region Variables
    @FXML
    private Label formText, welcomeText;
    @FXML
    private Button login, signup;
    @FXML
    private TextField username, email, password, confirmPassword;
    @FXML
    private Button forgotPassword, formButton, resetPasswordButton;
    @FXML
    private AnchorPane formPage, dashboardPage;
    // endregion
    // region Form
    @FXML
    private void ChangeForm() {
        ObservableList<String> shortLogin = login.getStyleClass(), shortSignUp = signup.getStyleClass();
        if (shortLogin.contains("active")) { // switching to signup
            formText.setText("Signup Form");
            shortLogin.remove("active");
            shortLogin.add("notActive");
            shortSignUp.remove("notActive");
            shortSignUp.add("active");
            confirmPassword.setVisible(true);
            formButton.setText("Sign Up");
            forgotPassword.setVisible(false);
        } else /*if (shortSignUp.contains("active"))*/ { // switching to login
            formText.setText("Login Form");
            formButton.setText("Login");
            shortSignUp.remove("active");
            if (!shortSignUp.contains("notActive"))
                shortSignUp.add("notActive");
            shortLogin.remove("notActive");
            shortLogin.add("active");
            confirmPassword.setVisible(false);
            formButton.setText("Login");
            password.setPromptText("Password:");
            forgotPassword.setVisible(true);
        }
        ClearForm();
    }
    
    @FXML
    private void FormSubmit() {
        if (ValidForm()) {
            try {
                String name = (signup.getStyleClass().contains("active")) ? SQLUtils.Register(username.getText(), password.getText(), email.getText()) : SQLUtils.Login(username.getText(), password.getText(), email.getText());
                formPage.setVisible(false);
                dashboardPage.setVisible(true);
                welcomeText.setText("Welcome, " + name);
                ClearForm();
            } catch (Exception ignored) {
                ErrorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information from MainController", "There was an error retrieving the SQL information, or that user doesn't exist.");
            }
        }
    }
    
    @FXML
    private void Forgot() {
        forgotPassword.setVisible(false);
        resetPasswordButton.setVisible(true);
        forgotPassword.setVisible(true);
        formText.setText("Forgot Password");
        formButton.setVisible(false);
        password.setPromptText("Enter New Password:");
        
        ObservableList<String> shortLogin = login.getStyleClass();
        if (shortLogin.contains("active") && !shortLogin.contains("notActive")) {
            shortLogin.remove("active");
            shortLogin.add("notActive");
        }
    }
    @FXML
    private void ResetPassword() {
        if (ValidForm()) {
            resetPasswordButton.setVisible(false);
            formButton.setVisible(true);
            forgotPassword.setVisible(true);
            formButton.setVisible(true);
            password.setPromptText("Password:");
            
            ObservableList<String> shortLogin = login.getStyleClass();
            formText.setText("Login Form");
            shortLogin.remove("notActive");
            shortLogin.add("active");
            SQLUtils.ResetPassword(username.getText(), password.getText(), email.getText());
            ClearForm();
        }
    }
    
    // endregion
    // region Utils
    private void ClearForm() {
        username.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
    }
    
    private boolean ValidForm() {
        // region Regex Characters
        // . any single character
        // * 0 or more occurrences of the preceding element
        // + 1 or more occurrence of the preceding element
        // [] match any character inside brackets
        // ^ start of a string
        // $ end of a string
        // \ escape character
        // ?=* positive look ahead assertion
        // ?! negative look ahead assertion
        // .{8, } at least 8 characters
        // \\d shortcut for 0-9
        //endregion
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9._]+\\.[a-zA-Z]{2,6}$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[/~`!@#$%^&*()_+{};:',<.>? =]).{8,}$";
        
        if (username.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || (signup.getStyleClass().contains("active") && confirmPassword.getText().isEmpty())) {
            ErrorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
            return false;
        } else if (!Pattern.compile(emailRegex).matcher(email.getText()).matches()) {
            ErrorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Email", "Please Enter A Valid Email That Contains An '@' And A '.com'");
            return false;
        } else if (!Pattern.compile(passwordRegex).matcher(password.getText()).matches()) {
            ErrorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Password", "Please Enter A Valid Password That Contains At Least 8 Characters, 1 Uppercase, 1 Lowercase, 1 Number, and 1 Special Character");
            return false;
        } else if (signup.getStyleClass().contains("active") && !password.getText().equals(confirmPassword.getText())) {
            ErrorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Passwords Must Match", "Password And Confirm Password Must Match");
            return false;
        } else if (!SQLUtils.ValidInfo(username.getText(), password.getText(), email.getText())) {
            ErrorAlert(Alert.AlertType.ERROR, "Invalid Info", "That User Does Not Exist", "Please enter valid information for a user that does already exist.");
            return false;
        }
        return true;
    }
    public static void ErrorAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    @FXML
    private void LogOut() {
        formPage.setVisible(true);
        dashboardPage.setVisible(false);
        welcomeText.setText("Welcome, NAME HERE");
    }
    
    // endregion
    // region Window Settings
    @FXML
    private void Minimize(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }
    
    @FXML
    private void Close() {
        System.exit(0);
    }
    
    // endregion
}