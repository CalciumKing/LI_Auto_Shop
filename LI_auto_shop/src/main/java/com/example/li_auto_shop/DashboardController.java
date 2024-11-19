package com.example.li_auto_shop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

// To Do:
// make a yes or no alert box that will delete an item if yes or do nothing if no
// make utils not static anymore
// remove all static methods
// put submit/login/create user function under enter key pressed in password section
// make one single sql table instead of two
// restructure database and sql code for new requirements
// make sure everything is camelCase and not PascalCase

public class DashboardController implements Initializable {
    // region Variables
    private Image image;
    @FXML
    private ImageView imageView;
    @FXML
    private String imageFolderPath = System.getProperty("user.dir") + "\\bin\\Images";
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane dashboard, welcomePage, databasePage, scannerPage;
    // region Table Variables
    @FXML
    private Button add_btn, update_btn, delete_btn;
    @FXML
    private TextField id_field, brand_field, model_field, price_field, quantity_field, reorder_field, scanner_id_field;
    @FXML
    private Spinner<Integer> spinner = new Spinner<>(-99, 99, 1);
    @FXML
    private TableView<Item> table, scanner_table;
    @FXML
    private TableColumn<Item, String> id_col, brand_col, model_col, scanner_id_col;
    @FXML
    private TableColumn<Item, Integer> on_hand_col, reorder_level_col, scanner_quantity_col;
    @FXML
    private TableColumn<Item, Double> price_col, scanner_price_col;
    @FXML
    private ObservableList<Item> items, scannerItems;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        brand_col.setCellValueFactory(new PropertyValueFactory<>("brand"));
        model_col.setCellValueFactory(new PropertyValueFactory<>("model"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        on_hand_col.setCellValueFactory(new PropertyValueFactory<>("on_hand"));
        reorder_level_col.setCellValueFactory(new PropertyValueFactory<>("reorder_level"));
        
        clearForm();
        items = new SQLUtils().refreshTable();
        table.setItems(items);
        
        scanner_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        scanner_quantity_col.setCellValueFactory(new PropertyValueFactory<>("on_hand"));
        scanner_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        clearScannerForm();
    }
    // endregion
    // endregion
    // region Side NavBar
    @FXML
    private void welcomePage() {
        welcomePage.setVisible(true);
        databasePage.setVisible(false);
        scannerPage.setVisible(false);
    }
    @FXML
    private void databasePage() {
        welcomePage.setVisible(false);
        databasePage.setVisible(true);
        scannerPage.setVisible(false);
    }
    @FXML
    private void scannerPage() {
        welcomePage.setVisible(false);
        databasePage.setVisible(false);
        scannerPage.setVisible(true);
    }
    // endregion
//    public void welcomeName(String name) {
//        welcomeText.setText("Welcome, " + name);
//    }
    // region Form
    @FXML
    private void selected() {
        Item item = table.getSelectionModel().getSelectedItem();
        if(item == null)
            return;
        
        id_field.setText(item.getId());
        brand_field.setText(item.getBrand());
        model_field.setText(item.getModel());
        price_field.setText(String.valueOf(item.getPrice()));
        quantity_field.setText(String.valueOf(item.getOn_hand()));
        reorder_field.setText(String.valueOf(item.getReorder_level()));
    }
    @FXML
    private void switchFormAction(ActionEvent event) {
        switch (((Button) event.getSource()).getText()) {
            case "Add Item" -> switcher(1);
            case "Update Item" -> switcher(2);
            case "Delete Item" -> switcher(3);
        }
        clearForm();
    }
    private void switcher(int toActivate) {
        ObservableList<String> shortAdd = add_btn.getStyleClass(), shortUpdate = update_btn.getStyleClass(), shortDelete = delete_btn.getStyleClass();
        shortAdd.clear();
        shortUpdate.clear();
        shortDelete.clear();
        
        switch (toActivate) {
            case 1:
                shortAdd.add("active");
                shortAdd.remove("notActive");
                shortUpdate.add("notActive");
                shortDelete.add("notActive");
                break;
            case 2:
                shortAdd.add("notActive");
                shortUpdate.add("active");
                shortUpdate.remove("notActive");
                shortDelete.add("notActive");
                break;
            case 3:
                shortAdd.add("notActive");
                shortUpdate.add("notActive");
                shortDelete.add("active");
                shortDelete.remove("notActive");
                break;
        }
    }
    @FXML
    private void submit() {
        SQLUtils sqlUtils = new SQLUtils();
        
        if (id_field.getText().isEmpty() || brand_field.getText().isEmpty() || model_field.getText().isEmpty() ||
                price_field.getText().isEmpty() || quantity_field.getText().isEmpty() || reorder_field.getText().isEmpty()) {
            Utils.errorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
        } else {
            if (add_btn.getStyleClass().contains("active"))
                sqlUtils.addItem(id_field.getText(), brand_field.getText(),
                        model_field.getText(), Double.parseDouble(price_field.getText()),
                        Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
            else if (update_btn.getStyleClass().contains("active"))
                sqlUtils.updateItem(id_field.getText(), brand_field.getText(),
                        model_field.getText(), Double.parseDouble(price_field.getText()),
                        Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
            else if (delete_btn.getStyleClass().contains("active")) {
                
                Utils.errorAlert(Alert.AlertType.CONFIRMATION, "Form Validation", "Delete This Item", "Confirming, would you like to delete this piece of data?");
                sqlUtils.deleteItem(id_field.getText());
            }
            
            items = sqlUtils.refreshTable();
            table.setItems(items);
            clearForm();
        }
    }
    @FXML
    private void loadItem() {
        String path = new SQLUtils().getImage(scanner_id_field.getText());
        if(path == null)
            System.out.println("No image available");
        else {
            File imageDir = new File(imageFolderPath);
            System.out.println(imageDir + path);
            File imageFile = new File(imageDir, path);
            image = new Image(imageFile.toURI().toString(), 125, 165, true, true);
            imageView.setImage(image);
        }
        Item item = new SQLUtils().getItem();
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
    private void clearScannerForm() {
        scanner_id_field.clear();
//        SpinnerValueFactory<Integer> qtySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(-99, 99, 1);
//        spinner.setValueFactory(qtySpinner);
        spinner.getValueFactory().setValue(0);
    }
    // endregion
    @FXML
    private void logOut() {
        Utils.changeScene("signup-login.fxml");
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