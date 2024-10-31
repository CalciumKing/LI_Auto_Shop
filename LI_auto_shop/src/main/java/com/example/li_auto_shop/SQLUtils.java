package com.example.li_auto_shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class SQLUtils {
    private static final Utils utils = new Utils();
    // region Login/Signup
    public void Login(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?;";
        RunFormSQL(sql, username, password, email, true);
    }
    public void Register(String username, String password, String email) {
        String sql = "insert into users_table (username, password, email) values (?, ?, ?);";
        RunFormSQL(sql, username, password, email, false);
    }
    public void ResetPassword(String username, String newPassword, String email) {
        String sql = "update users_table set password=? where username=? and email=?;";
        RunFormSQL(sql, newPassword, username, email, false);
    }
    public boolean ValidInfo(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?";
        Connection connect = ConnectDB();
        if (connect == null)
            return false;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, email);
            ResultSet result = prepared.executeQuery();
            System.out.println("prepared statements executed successfully");
            
            if (result.next()) {
                if (result.getString("username").equals(username) && result.getString("password").equals(password) && result.getString("email").equals(email)) {
                    System.out.println("passes test");
//                    new DashboardController().WelcomeName(result.getString("username"));
                    return true;
                }
            }
            return false;
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
            System.out.println("catch");
            return false;
        }
    }
    // endregion
    // region Table
    public void AddItem(String id, String vehicle_type, String year, String make, String model, double price, int quantity_in_stock, int reorder_level) {
        String sql = "insert into items (id, vehicle_type, year, make, model, price, quantity_in_stock, reorder_level) values (?, ?, ?, ?, ?, ?, ?, ?);";
        RunAutoSQL(sql, id, vehicle_type, year, make, model, price, quantity_in_stock, reorder_level);
    }
    public void UpdateItem(String id, String vehicle_type, String year, String make, String model, double price, int quantity_in_stock, int reorder_level) {
        String sql = "update items set id = ?, vehicle_type = ?, year = ?, make = ?, model = ?, price = ?, quantity_in_stock = ?, reorder_level = ? where id = ?;";
        Connection connect = ConnectAutoDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            PreparedStatement newPrepared = autoEight(prepared, id, vehicle_type, year, make, model, price, quantity_in_stock, reorder_level);
            if (newPrepared == null)
                return;
            
            newPrepared.setString(9, id);
            newPrepared.executeUpdate();
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Inserting Information", "There was an error running the SQL information to add to the table.");
        }
    }
    public void DeleteItem(String id) {
        String sql = "delete from items where id = ?;";
        Connection connect = ConnectAutoDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Information", "There was an error in the process of deleting information from the table.");
        }
    }
    public ObservableList<Item> RefreshTable() {
        String sql = "select * from items";
        Connection connect = ConnectAutoDB();
        if (connect == null)
            return null;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            ResultSet result = prepared.executeQuery();
            ObservableList<Item> data = FXCollections.observableArrayList();
            while (result.next()) {
                Item thing = new Item(result.getString("id"), result.getString("vehicle_type"),
                        result.getString("year"), result.getString("make"), result.getString("model"),
                        result.getDouble("price"), result.getInt("quantity_in_stock"), result.getInt("reorder_level"));
                data.add(thing);
            }
            return data;
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Refreshing Table", "There was an error running the SQL information to refresh the table.");
            return null;
        }
    }
    // endregion
    // region Utils
    private static Connection ConnectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/login_and_register", "root", "password");
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Connection Error", "Error Connecting To Login Database", "Database could not be connected to, please try again.");
            return null;
        }
    }
    private static Connection ConnectAutoDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/auto_shop", "root", "password");
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Connection Error", "Error Connecting To Auto Database", "Database could not be connected to, please try again.");
            return null;
        }
    }
    private static void RunFormSQL(String sql, String username, String password, String email, boolean query) {
        Connection connect = ConnectDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, email);
            if (query)
                prepared.executeQuery();
            else
                prepared.executeUpdate();
        } catch (SQLException ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information, from RunFormSQL", "There was an error retrieving the SQL information.");
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
    }
    private static void RunAutoSQL(String sql, String id, String vehicle_type,
                                   String year, String make, String model, double price,
                                   int quantity_in_stock, int reorder_level) {
        Connection connect = ConnectAutoDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            PreparedStatement newPrepared = autoEight(prepared, id, vehicle_type, year, make, model, price, quantity_in_stock, reorder_level);
            if (newPrepared == null)
                return;
            
            newPrepared.executeUpdate();
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Inserting Information", "There was an error running the SQL information to add to the table.");
        }
    }
    private static PreparedStatement autoEight(PreparedStatement prepared, String id, String vehicle_type,
                                               String year, String make, String model, double price,
                                               int quantity_in_stock, int reorder_level) {
        try {
            prepared.setString(1, id);
            prepared.setString(2, vehicle_type);
            prepared.setString(3, year);
            prepared.setString(4, make);
            prepared.setString(5, model);
            prepared.setDouble(6, price);
            prepared.setInt(7, quantity_in_stock);
            prepared.setInt(8, reorder_level);
            return prepared;
        } catch (Exception ignored) {
            utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Entering Auto Application Information", "There was an error entering the information in the Auto Eight class");
            return null;
        }
    }
    // endregion
}