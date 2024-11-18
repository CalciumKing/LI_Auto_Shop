package com.example.li_auto_shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class SQLUtils {
    private static final Utils utils = new Utils();
    // region Login/Signup
    public void login(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?;";
        runFormSQL(sql, username, password, email, true);
    }
    public void register(String username, String password, String email) {
        String sql = "insert into users_table (username, password, email) values (?, ?, ?);";
        runFormSQL(sql, username, password, email, false);
    }
    public void resetPassword(String username, String newPassword, String email) {
        String sql = "update users_table set password=? where username=? and email=?;";
        runFormSQL(sql, newPassword, username, email, false);
    }
    public boolean validInfo(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?";
        Connection connect = connectDB();
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
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
            System.out.println("catch");
            return false;
        }
    }
    // endregion
    // region Table
    public void addItem(String id, String brand, String model, double price, int on_hand, int reorder_level) {
        String sql = "insert into items (id, brand, model_number, price, on_hand, reorder_level) values (?, ?, ?, ?, ?, ?);";
        runAutoSQL(sql, id, brand, model, price, on_hand, reorder_level);
    }
    public void updateItem(String id, String brand, String model, double price, int on_hand, int reorder_level) {
        String sql = "update items set id = ?, brand = ?, model_number = ?, price = ?, on_hand = ?, reorder_level = ? where (id = ?);";
        Connection connect = connectDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql);
             PreparedStatement newPrepared = itemPrepared(prepared, id, brand, model, price, on_hand, reorder_level)) {
            if (newPrepared == null)
                return;
            newPrepared.setString(7, id);
            newPrepared.executeUpdate();
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Inserting Information", "There was an error running the SQL information to add to the table.");
        }
    }
    public void deleteItem(String id) {
        String sql = "delete from items where id = ?;";
        Connection connect = connectDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Information", "There was an error in the process of deleting information from the table.");
        }
    }
    public ObservableList<Item> refreshTable() {
        String sql = "select * from items;";
        Connection connect = connectDB();
        if (connect == null)
            return null;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            ResultSet result = prepared.executeQuery();
            ObservableList<Item> data = FXCollections.observableArrayList();
            while (result.next()) {
                Item thing = new Item(result.getString("id"), result.getString("brand"),
                        result.getString("model"), result.getDouble("price"),
                        result.getInt("on_hand"), result.getInt("reorder_level"));
                data.add(thing);
            }
            return data;
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Refreshing Table", "There was an error running the SQL information to refresh the table.");
            return null;
        }
    }
    // endregion
    // region Utils
    private static Connection connectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/auto_shop", "root", "password");
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Connection Error", "Error Connecting To Auto Database", "Database could not be connected to, please try again.");
            return null;
        }
    }
    private static void runFormSQL(String sql, String username, String password, String email, boolean query) {
        Connection connect = connectDB();
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
            utils.errorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information, from RunFormSQL", "There was an error retrieving the SQL information.");
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
    }
    private static void runAutoSQL(String sql, String id, String brand,
                                   String model, double price,
                                   int on_hand, int reorder_level) {
        Connection connect = connectDB();
        if (connect == null)
            return;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql);
             PreparedStatement newPrepared = itemPrepared(prepared, id, brand, model, price, on_hand, reorder_level)) {
            if (newPrepared == null)
                return;
            newPrepared.executeUpdate();
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error In runAutoSQL", "There was an error running the SQL information to add to the table.");
        }
    }
    private static PreparedStatement itemPrepared(PreparedStatement prepared, String id, String brand,
                                               String model, double price,
                                               int on_hand, int reorder_level) {
        try {
            prepared.setString(1, id);
            prepared.setString(2, brand);
            prepared.setString(3, model);
            prepared.setDouble(4, price);
            prepared.setInt(5, on_hand);
            prepared.setInt(6, reorder_level);
            return prepared;
        } catch (Exception ignored) {
            utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Entering Auto Application Information", "There was an error entering the information in the Auto Eight class");
            return null;
        }
    }
    // endregion
}