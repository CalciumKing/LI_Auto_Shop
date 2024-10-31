package com.example.li_auto_shop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    // region Variables
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane dashboard, welcomePage, databasePage;
    // region Table Variables
    @FXML
    private Button add_btn, update_btn, delete_btn;
    @FXML
    private TextField id_field, vehicleType_field, year_field, make_field, model_field, price_field, quantity_field, reorder_field;
    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, String> id_col, vehicle_type_col, make_col, model_col;
    @FXML
    private TableColumn<Item, Integer> year_col, quantity_in_stock_col, reorder_level_col;
    @FXML
    private TableColumn<Item, Double> price_col;
    @FXML
    private ObservableList<Item> items;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicle_type_col.setCellValueFactory(new PropertyValueFactory<>("vehicle_type"));
        year_col.setCellValueFactory(new PropertyValueFactory<>("year"));
        make_col.setCellValueFactory(new PropertyValueFactory<>("make"));
        model_col.setCellValueFactory(new PropertyValueFactory<>("model"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity_in_stock_col.setCellValueFactory(new PropertyValueFactory<>("quantity_in_stock"));
        reorder_level_col.setCellValueFactory(new PropertyValueFactory<>("reorder_level"));
        ClearForm();
        items = new SQLUtils().RefreshTable();
        table.setItems(items);
    }
    // endregion
    // endregion
    // region Side NavBar
    @FXML
    void WelcomePage() {
        welcomePage.setVisible(true);
        databasePage.setVisible(false);
    }
    @FXML
    void DatabasePage(ActionEvent event) {
        welcomePage.setVisible(false);
        databasePage.setVisible(true);
    }
    // endregion
    public void WelcomeName(String name) {
        welcomeText.setText("Welcome, " + name);
    }
    // region Form
    @FXML
    private void Selected() {
        Item item = table.getSelectionModel().getSelectedItem();
        id_field.setText(item.getId());
        vehicleType_field.setText(item.getVehicle_type());
        year_field.setText(item.getYear());
        make_field.setText(item.getMake());
        model_field.setText(item.getModel());
        price_field.setText(String.valueOf(item.getPrice()));
        quantity_field.setText(String.valueOf(item.getQuantity_in_stock()));
        reorder_field.setText(String.valueOf(item.getReorder_level()));
    }
    @FXML
    private void SwitchFormAction(ActionEvent event) {
        switch (((Button) event.getSource()).getText()) {
            case "Add Item" -> Switcher(1);
            case "Update Item" -> Switcher(2);
            case "Delete Item" -> Switcher(3);
        }
        ClearForm();
    }
    private void Switcher(int toActivate) {
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
    private void Submit() {
        SQLUtils sqlUtils = new SQLUtils();
        Utils utils = new Utils();
        
        if (id_field.getText().isEmpty() || vehicleType_field.getText().isEmpty() || year_field.getText().isEmpty() ||
                make_field.getText().isEmpty() || model_field.getText().isEmpty() || price_field.getText().isEmpty() ||
                quantity_field.getText().isEmpty() || reorder_field.getText().isEmpty()) {
            utils.ErrorAlert(Alert.AlertType.INFORMATION, "Form Validation", "Invalid Fields", "All Fields Must Be Filled In");
        } else {
            if (add_btn.getStyleClass().contains("active"))
                sqlUtils.AddItem(id_field.getText(), vehicleType_field.getText(), year_field.getText(),
                        make_field.getText(), model_field.getText(), Double.parseDouble(price_field.getText()),
                        Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
            else if (update_btn.getStyleClass().contains("active"))
                sqlUtils.UpdateItem(id_field.getText(), vehicleType_field.getText(), year_field.getText(),
                        make_field.getText(), model_field.getText(), Double.parseDouble(price_field.getText()),
                        Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
            else if (delete_btn.getStyleClass().contains("active")) {
                // make a yes or no alert box that will delete an item if yes or do nothing if no
                // make utils not static anymore
                // remove all static methods
                utils.ErrorAlert(Alert.AlertType.CONFIRMATION, "Form Validation", "Delete This Item", "Confirming, would you like to delete this piece of data?");
                sqlUtils.DeleteItem(id_field.getText());
            }
            
            items = sqlUtils.RefreshTable();
            table.setItems(items);
            ClearForm();
        }
    }
    @FXML
    private void ClearForm() {
        id_field.clear();
        vehicleType_field.clear();
        year_field.clear();
        make_field.clear();
        model_field.clear();
        price_field.clear();
        quantity_field.clear();
        reorder_field.clear();
    }
    // endregion
    @FXML
    private void LogOut() {
        utils.ChangeScene("signup-login.fxml");
        welcomeText.getScene().getWindow().hide();
//        WelcomeName("This is a test");
    }
    // region Window Settings
    @FXML
    private void Minimize(ActionEvent event) {
        utils.Minimize(event);
    }
    @FXML
    private void Close() {
        utils.Close();
    }
    @FXML
    private void Click(MouseEvent event) {
        utils.WindowClick(event);
    }
    @FXML
    private void Drag(MouseEvent event) {
        utils.WindowDrag(event, dashboard);
    }
    // endregion
}