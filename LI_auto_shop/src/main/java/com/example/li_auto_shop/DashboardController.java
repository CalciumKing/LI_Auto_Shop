package com.example.li_auto_shop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    // region Variables
    @FXML
    private ImageView imageView, user_img;
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane dashboard, welcomePage, databasePage, scannerPage, userPage;
    // region Table Variables
    @FXML
    private TextField id_field, brand_field, model_field, price_field, quantity_field, reorder_field, scanner_id_field, username_field, email_field;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private TableView<Item> table, scanner_table;
    @FXML
    private TableColumn<Item, String> id_col, brand_col, model_col, scanner_id_col;
    @FXML
    private TableColumn<Item, Integer> on_hand_col, reorder_level_col, scanner_quantity_col;
    @FXML
    private TableColumn<Item, Double> price_col, scanner_price_col;
    @FXML
    private TableView<User> user_table;
    @FXML
    private TableColumn<User, String> username_col, email_col;
    @FXML
    private TableColumn<User, Integer> grade_col;
    @FXML
    private ObservableList<Item> items, scannerItems;
    @FXML
    private ObservableList<User> userItems;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        brand_col.setCellValueFactory(new PropertyValueFactory<>("brand"));
        model_col.setCellValueFactory(new PropertyValueFactory<>("model"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        on_hand_col.setCellValueFactory(new PropertyValueFactory<>("on_hand"));
        reorder_level_col.setCellValueFactory(new PropertyValueFactory<>("reorder_level"));
        
        clearForm();
        items = SQLUtils.refreshTable();
        table.setItems(items);
        
        scanner_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        scanner_quantity_col.setCellValueFactory(new PropertyValueFactory<>("on_hand"));
        scanner_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        SpinnerValueFactory<Integer> qtySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(-100, 100, 1);
        spinner.setValueFactory(qtySpinner);
        clearScannerForm();
        
        username_col.setCellValueFactory(new PropertyValueFactory<>("username"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        grade_col.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        clearUsersForm();
        userItems = SQLUtils.refreshUserTable();
        user_table.setItems(userItems);
    }
    // endregion
    // endregion
    
    // region Side NavBar
    @FXML
    private void welcomePage() {
        welcomePage.setVisible(true);
        databasePage.setVisible(false);
        scannerPage.setVisible(false);
        userPage.setVisible(false);
    }
    
    @FXML
    private void databasePage() {
        welcomePage.setVisible(false);
        databasePage.setVisible(true);
        scannerPage.setVisible(false);
        userPage.setVisible(false);
    }
    
    @FXML
    private void scannerPage() {
        welcomePage.setVisible(false);
        databasePage.setVisible(false);
        scannerPage.setVisible(true);
        userPage.setVisible(false);
    }
    
    @FXML
    private void usersPage() {
        welcomePage.setVisible(false);
        databasePage.setVisible(false);
        scannerPage.setVisible(false);
        userPage.setVisible(true);
    }
    // endregion
    
    public void welcomeName(String name) {
        welcomeText.setText("Welcome, " + name);
    }
    
    // region Form
    @FXML
    private void selected() {
        Item item = table.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        
        id_field.setText(item.getId());
        brand_field.setText(item.getBrand());
        model_field.setText(item.getModel());
        price_field.setText(String.valueOf(item.getPrice()));
        quantity_field.setText(String.valueOf(item.getOn_hand()));
        reorder_field.setText(String.valueOf(item.getReorder_level()));
    }
    
    @FXML
    private void addItem() {
        if (itemFormInvalid())
            return;
        
        String id = id_field.getText();
        String brand = brand_field.getText();
        String model = model_field.getText();
        double price = Double.parseDouble(price_field.getText());
        int quantity = Integer.parseInt(quantity_field.getText());
        int reorder = Integer.parseInt(reorder_field.getText());
        
        SQLUtils.addItem(id, brand, model, price, quantity, reorder);
        
        items = SQLUtils.refreshTable();
        table.setItems(items);
        clearForm();
    }
    
    @FXML
    private void updateItem() {
        if (itemFormInvalid())
            return;
        
        String id = id_field.getText();
        String brand = brand_field.getText();
        String model = model_field.getText();
        double price = Double.parseDouble(price_field.getText());
        int quantity = Integer.parseInt(quantity_field.getText());
        int reorder = Integer.parseInt(reorder_field.getText());
        
        SQLUtils.updateItem(id, brand, model, price, quantity, reorder);
        
        items = SQLUtils.refreshTable();
        table.setItems(items);
        clearForm();
    }
    
    @FXML
    private void deleteItem() {
        if (itemFormInvalid())
            return;
        
        Optional<ButtonType> optionSelected = Utils.confirmAlert(Alert.AlertType.CONFIRMATION, "Form Validation", "Delete This Item", "Confirming, would you like to delete this piece of data?");
        if (optionSelected.isPresent() && optionSelected.get().getText().equals("Yes")) {
            SQLUtils.deleteItem(id_field.getText());
            
            items = SQLUtils.refreshTable();
            table.setItems(items);
            clearForm();
        }
    }
    
    private boolean itemFormInvalid() {
        if (id_field.getText().isEmpty() || brand_field.getText().isEmpty() ||
                model_field.getText().isEmpty() || price_field.getText().isEmpty() ||
                quantity_field.getText().isEmpty() || reorder_field.getText().isEmpty()) {
            Utils.errorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
            return true;
        }
        return false;
    }
    
    @FXML
    private void clearForm() {
        id_field.clear();
        brand_field.clear();
        model_field.clear();
        price_field.clear();
        quantity_field.clear();
        reorder_field.clear();
    }
    // endregion
    
    // region Scanner Form
    @FXML
    private void loadItem() {
        if (scanner_id_field.getText().isEmpty()) {
            clearScannerForm();
            return;
        }
        
        Item item = SQLUtils.getItem(scanner_id_field.getText());
        if (item == null) {
            Utils.createImage("fileNotFound.png", imageView);
            spinner.getValueFactory().setValue(0);
            return;
        }
        Utils.createImage(item.getPath(), imageView);
        spinner.getValueFactory().setValue(1);
    }
    
    @FXML
    private void clearScannerForm() {
        scanner_id_field.clear();
        Utils.createImage("defaultImage.jpg", imageView);
        spinner.getValueFactory().setValue(0);
    }
    
    @FXML
    private void submitScanner() {
        if (scanner_id_field.getText().isEmpty() || spinner.getValue() == 0) {
            Utils.errorAlert(Alert.AlertType.INFORMATION, "Adjustment Validation", "Invalid Fields", "All Fields Must Be Filled In And Have A Non-Zero Value");
            return;
        }
        
        Item databaseItem = SQLUtils.getItem(scanner_id_field.getText());
        if (databaseItem == null) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Form Error", "Item Does Not Exist", "Make Sure ID Is Valid And Belongs To An Already Existing Item");
            return;
        }
        
        scannerItems = scanner_table.getItems();
        Item selectedItem = null;
        for (Item tableItem : scannerItems) {
            if (tableItem.getId().equals(databaseItem.getId())) {
                selectedItem = tableItem;
                break;
            }
        }
        
        if (selectedItem != null) {
            int newQuantity = selectedItem.getOn_hand() + spinner.getValue();
            if (newQuantity == 0) {
                scannerItems.remove(selectedItem);
                clearScannerForm();
                return;
            }
            selectedItem.setOn_hand(newQuantity);
            selectedItem.setPrice(databaseItem.getPrice() * newQuantity);
            scanner_table.refresh();
        } else {
            scannerItems.add(new Item(databaseItem.getId(), databaseItem.getBrand(), databaseItem.getModel(),
                    databaseItem.getPrice() * spinner.getValue(),
                    spinner.getValue(), databaseItem.getReorder_level(), databaseItem.getPath()));
            scanner_table.setItems(scannerItems);
        }
        
        clearScannerForm();
    }
    
    @FXML
    private void finalizeScannerForm() {
        for (Item scannerItem : scannerItems) {
            if (scannerItem == null)
                continue;
            
            String id = scannerItem.getId();
            Item sqlItem = SQLUtils.getItem(id);
            
            if (sqlItem == null)
                return;
            
            String brand = scannerItem.getBrand();
            String model = scannerItem.getModel();
            double price = sqlItem.getPrice();
            int quantity = sqlItem.getOn_hand() + scannerItem.getOn_hand();
            int reorder = scannerItem.getReorder_level();
            
            SQLUtils.updateItem(id, brand, model, price, quantity, reorder);
        }
        
        clearScannerForm();
        scannerItems.clear();
        scanner_table.setItems(scannerItems);
        items = SQLUtils.refreshTable();
        table.setItems(items);
    }
    
    @FXML
    private void selectedScanner() {
        Item item = scanner_table.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        
        Utils.createImage(item.getPath(), imageView);
        scanner_id_field.setText(item.getId());
        spinner.getValueFactory().setValue(1);
    }
    // endregion
    
    // region Users Form
    @FXML
    private void loadUser() {
        if (username_field.getText().isEmpty()) {
            clearUsersForm();
            return;
        }
        
        User user = SQLUtils.getUser(username_field.getText());
        if (user == null) {
            Utils.createImage("fileNotFound.png", user_img);
            return;
        }
        Utils.createImage(user.getImagePath(), user_img);
    }
    @FXML
    private void clearUsersForm() {
        username_field.clear();
        email_field.clear();
        Utils.createImage("defaultImage.jpg", user_img);
    }
    
    @FXML
    private void selectedUser() {
        User user = user_table.getSelectionModel().getSelectedItem();
        if (user == null)
            return;
        
        username_field.setText(user.getUsername());
        email_field.setText(user.getEmail());
//        grade_field.setText(user.getGrade());
        
        Utils.createImage(user.getImagePath(), user_img);
    }
    
    @FXML
    private void addUser() {
        if (userFormInvalid() || userAlreadyExists())
            return;
        
        String username = username_field.getText();
        String email = email_field.getText();
//        int grade = Integer.parseInt(grade_field.getText());
        
        SQLUtils.addUser(username, email, "", -1);
//        SQLUtils.addUser(username, email, "", grade);
        
        clearUsersForm();
        userItems = SQLUtils.refreshUserTable();
        user_table.setItems(userItems);
    }
    
    @FXML
    private void updateUser() {
        if (userFormInvalid())
            return;
        
        String username = username_field.getText();
        String email = email_field.getText();
//        int grade = Integer.parseInt(grade_field.getText());
        
        SQLUtils.updateUser(username, email, "", -1);
//        SQLUtils.updateUser(username, email, "", grade);
        
        clearUsersForm();
        userItems = SQLUtils.refreshUserTable();
        user_table.setItems(userItems);
    }
    
    @FXML
    private void deleteUser() {
        if (userFormInvalid())
            return;
        
        Optional<ButtonType> optionSelected = Utils.confirmAlert(Alert.AlertType.CONFIRMATION, "Form Validation", "Delete This User", "Confirming, would you like to delete this piece of data?");
        if (optionSelected.isPresent() && optionSelected.get().getText().equals("Yes")) {
            SQLUtils.deleteUser(username_field.getText());
            
            clearUsersForm();
            userItems = SQLUtils.refreshUserTable();
            user_table.setItems(userItems);
        }
    }
    
    private boolean userFormInvalid() {
        String username = username_field.getText();
        String email = email_field.getText();
        
        if (username.isEmpty() || email.isEmpty()) {
            Utils.errorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
            return true;
        }
        return false;
    }
    // endregion
    
    private boolean userAlreadyExists() {
        String username = username_field.getText();
        
        if (SQLUtils.userExists(username)) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Invalid Info", "That User Already Exists", "Please enter information for a user that does not already exist.");
            return true;
        }
        return false;
    }
    
    @FXML
    private void logOut() {
        Utils.changeScene("signup-login.fxml", null);
        welcomeText.getScene().getWindow().hide();
    }
    
    // region Window Settings
    @FXML
    private void Minimize(ActionEvent event) {
        Utils.windowMinimize(event);
    }
    
    @FXML
    private void Close() {
        Utils.windowClose();
    }
    
    @FXML
    private void Click(MouseEvent event) {
        Utils.windowClick(event);
    }
    
    @FXML
    private void Drag(MouseEvent event) {
        Utils.windowDrag(event, dashboard);
    }
    // endregion
}