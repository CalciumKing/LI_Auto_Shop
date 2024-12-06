package com.example.li_auto_shop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SignupLoginController {
    // region Variables
    @FXML
    private AnchorPane page;
    @FXML
    private Label formText;
    @FXML
    private Button login, signup;
    @FXML
    private TextField username, email;
    @FXML
    private PasswordField password, confirmPassword;
    @FXML
    private Button forgotPassword, formButton, resetPasswordButton;
    // endregion
    
    // region Form
    @FXML
    private void changeForm() {
        ObservableList<String> shortLogin = login.getStyleClass(), shortSignUp = signup.getStyleClass();
        if (shortLogin.contains("active")) { // switching to signup
            formText.setText("Signup Form");
            shortLogin.remove("active");
            shortLogin.add("notActive");
            shortSignUp.remove("notActive");
            shortSignUp.add("active");
            
            email.setVisible(true);
            confirmPassword.setVisible(true);
            formButton.setText("Sign Up");
            forgotPassword.setVisible(false);
            
        } else { // switching to login
            formText.setText("Login Form");
            formButton.setText("Login");
            shortSignUp.remove("active");
            if (!shortSignUp.contains("notActive"))
                shortSignUp.add("notActive");
            shortLogin.remove("notActive");
            shortLogin.add("active");
            
            email.setVisible(false);
            confirmPassword.setVisible(false);
            formButton.setText("Login");
            password.setPromptText("Password:");
            forgotPassword.setVisible(true);
            resetPasswordButton.setVisible(false);
            formButton.setVisible(true);
        }
        
        clearForm();
    }
    
    @FXML
    private void formSubmit() {
        if (validForm()) {
            try {
                User user = (signup.getStyleClass().contains("active")) ?
                        SQLUtils.register(username.getText(), password.getText(), email.getText()) :
                        SQLUtils.login(username.getText(), password.getText());
                
                if (user == null) {
                    Utils.errorAlert(Alert.AlertType.ERROR, "Null User", "That User Does Not Exist", "Please enter information for a user that does already exist.");
                    return;
                }
                
                Utils.changeScene("dashboard.fxml", user);
                page.getScene().getWindow().hide();
                clearForm();
            } catch (Exception ignored) {
                Utils.errorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information from MainController", "There was an error retrieving the SQL information, or that user doesn't exist.");
            }
        }
    }
    
    @FXML
    private void forgotPassword() { // changing to the forgot password section
        email.setVisible(true);
        resetPasswordButton.setVisible(true);
        forgotPassword.setVisible(true);
        forgotPassword.setVisible(false);
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
    private void resetPassword() {
        if (validForm()) { // sends user back to log in section after submission
            email.setVisible(false);
            resetPasswordButton.setVisible(false);
            formButton.setVisible(true);
            forgotPassword.setVisible(true);
            password.setPromptText("Password:");
            
            ObservableList<String> shortLogin = login.getStyleClass();
            formText.setText("Login Form");
            shortLogin.remove("notActive");
            shortLogin.add("active");
            
            SQLUtils.resetPassword(username.getText(), password.getText(), email.getText());
            clearForm();
        }
    }
    // endregion
    
    // region Form Utils
    private void clearForm() {
        username.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
    }
    
    private boolean validForm() {
        String name = username.getText(), pass = password.getText(), confirmPass = confirmPassword.getText(), mail = email.getText();
        
        if (isFormEmpty(name, pass, confirmPass, mail)) {
            Utils.errorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
            return false;
        } else if (signup.getStyleClass().contains("active")) {
            if (Utils.regexValidation(mail, pass))
                return false;
            else if (!pass.equals(confirmPass)) {
                Utils.errorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Passwords Must Match", "Password And Confirm Password Must Match");
                return false;
            } else if (SQLUtils.getUser(name) != null) {
                Utils.errorAlert(Alert.AlertType.ERROR, "Invalid Info", "That User Already Exists", "Please enter information for a user that does not already exist.");
                return false;
            }
        } else if (login.getStyleClass().contains("active") && SQLUtils.getUser(name) == null) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Invalid Info", "That User Does Not Exist", "Please enter valid information for a user that does already exists.");
            return false;
        }
        
        return true;
    }
    
    private boolean isFormEmpty(String username, String password, String confirmPass, String email) {
        return username.isEmpty() || password.isEmpty() ||
                (signup.getStyleClass().contains("active") && (confirmPass.isEmpty() || email.isEmpty()));
    }
    // endregion
    
    // region Window Settings
    @FXML
    private void windowMinimize(ActionEvent event) {
        Utils.windowMinimize(event);
    }
    
    @FXML
    private void windowClose() {
        Utils.windowClose();
    }
    
    @FXML
    private void windowClick(MouseEvent event) {
        Utils.windowClick(event);
    }
    
    @FXML
    private void windowDrag(MouseEvent event) {
        Utils.windowDrag(event, page);
    }
    // endregion
}