package com.example.li_auto_shop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class DashboardController  implements Initializable {
    // region Variables
    @FXML
    private static Label welcomeText;
    @FXML
    private AnchorPane welcomePage, databasePage;
    
    // region Table
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
    //  = FXCollections.observableArrayList(
    //            new Item("015", "Honda Civic", "2001", "Gen 3", "2.0", 35.00, 12, 2),
    //            new Item("015", "Honda Civic", "2001", "Gen 3", "2.0", 35.00, 12, 2),
    //            new Item("015", "Honda Civic", "2001", "Gen 3", "2.0", 35.00, 12, 2)
    //    )
    
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
        items = SQLUtils.RefreshTable();
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
    @FXML
    public static void WelcomeName(String name) {
        welcomeText.setText("Welcome, " + name);
    }
    // region Form
    @FXML
    private void SwitchFormAction(ActionEvent event) {
        String buttonText = ((Button) event.getSource()).getText();
        switch (buttonText) {
            case "Add Item" -> Switcher(1);
            case "Update Item" -> Switcher(2);
            case "Delete Item" -> Switcher(3);
        }
        ClearForm();
    }
    private void Switcher(int toActivate) {
        ObservableList<String> shortAdd = add_btn.getStyleClass(), shortUpdate = update_btn.getStyleClass(), shortDelete = delete_btn.getStyleClass();
//        shortAdd.remove("active");
//        shortUpdate.remove("active");
//        shortDelete.remove("active");
//        shortAdd.remove("notActive");
//        shortUpdate.remove("notActive");
//        shortDelete.remove("notActive");
        shortAdd.clear();
        shortUpdate.clear();
        shortDelete.clear();
        switch(toActivate) {
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
        System.out.println(shortAdd);
        System.out.println(shortUpdate);
        System.out.println(shortDelete);
        System.out.println();
    }
    @FXML
    private void Submit() {
        if(add_btn.getStyleClass().contains("active"))
            SQLUtils.AddItem(id_field.getText(), vehicleType_field.getText(), year_field.getText(), make_field.getText(), model_field.getText(), Double.parseDouble(price_field.getText()), Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
        else if(update_btn.getStyleClass().contains("active"))
            SQLUtils.UpdateItem(id_field.getText(), vehicleType_field.getText(), year_field.getText(), make_field.getText(), model_field.getText(), Double.parseDouble(price_field.getText()), Integer.parseInt(quantity_field.getText()), Integer.parseInt(reorder_field.getText()));
        else if(delete_btn.getStyleClass().contains("active"))
            SQLUtils.DeleteItem(id_field.getText());
        
        items = SQLUtils.RefreshTable();
        table.setItems(items);
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
        Utils.ChangeScene("signup-login.fxml");
        welcomeText.getScene().getWindow().hide();
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