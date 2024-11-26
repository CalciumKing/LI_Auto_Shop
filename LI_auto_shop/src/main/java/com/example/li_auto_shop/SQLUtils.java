package com.example.li_auto_shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class SQLUtils {
    // region Login/Signup
    public static User login(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?;";
        return runFormSQL(sql, username, password, email, true);
    }
    
    public static void register(String username, String password, String email) {
        String sql = "insert into users_table (username, password, email) values (?, ?, ?);";
        runFormSQL(sql, username, password, email, false);
    }
    
    public static void resetPassword(String username, String newPassword, String email) {
        String sql = "update users_table set password=? where username=? and email=?;";
        runFormSQL(sql, newPassword, username, email, false);
    }
    
    public static boolean validInfo(String username, String password, String email) {
        Connection connect = connectDB();
        if (connect == null) return false;
        
        String sql = "select * from users_table where username = ? and password = ? and email = ?;";
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, email);
            ResultSet result = prepared.executeQuery();
            
            if (result.next())
                return result.getString("username").equals(username) &&
                        result.getString("password").equals(password) &&
                        result.getString("email").equals(email);
            return false;
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
            System.out.println("catch");
            return false;
        }
    }
    // endregion
    
    // region Table
    public static void addItem(String id, String brand, String model, double price, int on_hand, int reorder_level) {
        Connection connect = connectDB();
        if (connect == null) return;
        
        String sql = "insert into items (id, brand, model_number, price, on_hand, reorder_level) values (?, ?, ?, ?, ?, ?);";
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.setString(2, brand);
            prepared.setString(3, model);
            prepared.setDouble(4, price);
            prepared.setInt(5, on_hand);
            prepared.setInt(6, reorder_level);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error In runAutoSQL", "There was an error running the SQL information to add to the table.");
        }
    }
    
    public static void updateItem(String id, String brand, String model, double price, int on_hand, int reorder_level) {
        Connection connect = connectDB();
        if (connect == null) return;
        
        String sql = "update items set id = ?, brand = ?, model_number = ?, price = ?, on_hand = ?, reorder_level = ? where id = ?;";
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.setString(2, brand);
            prepared.setString(3, model);
            prepared.setDouble(4, price);
            prepared.setInt(5, on_hand);
            prepared.setInt(6, reorder_level);
            prepared.setString(7, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Inserting Information", "There was an error running the SQL information to add to the table.");
        }
    }
    
    public static void deleteItem(String id) {
        Connection connect = connectDB();
        if (connect == null) return;
        
        String sql = "delete from items where id = ?;";
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Information", "There was an error in the process of deleting information from the table.");
        }
    }
    
    public static ObservableList<Item> refreshTable() {
        Connection connect = connectDB();
        if (connect == null) return null;
        
        String sql = "select * from items;";
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            ResultSet result = prepared.executeQuery();
            ObservableList<Item> data = FXCollections.observableArrayList();
            
            while (result.next()) {
                Item item = new Item(result.getString("id"), result.getString("brand"),
                        result.getString("model_number"), result.getDouble("price"),
                        result.getInt("on_hand"), result.getInt("reorder_level"), result.getString("image"));
                data.add(item);
            }
            return data;
            
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Refreshing Table", "There was an error running the SQL information to refresh the table.");
            return null;
        }
    }
    // endregion
    
    // region Scanner Table
    public static Item getItem(String id) {
        Connection connection = connectDB();
        if (connection == null) return null;
        
        String sql = "select * from items where id = ? limit 1;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, id);
            ResultSet result = prepared.executeQuery();
            if (result.next())
                return new Item(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getDouble(4),
                        result.getInt(5),
                        result.getInt(6),
                        result.getString(7)
                );
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Get Item Error", "Error Getting Item From Database", "There was an error getting an item from the database, please try again.");
        }
        return null;
    }
    // endregion
    
    // region Utils
    private static Connection connectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/auto_shop", "root", "password");
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Connection Error", "Error Connecting To Auto Database", "Database could not be connected to, please try again.");
            return null;
        }
    }
    
    private static User runFormSQL(String sql, String username, String password, String email, boolean query) {
        Connection connect = connectDB();
        if (connect == null)
            return null;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, email);
            
            if (query) {
                prepared.executeQuery();
                return new User(username, password, email);
            } else {
                prepared.executeUpdate();
                return null;
            }
            
        } catch (SQLException ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information, from RunFormSQL", "There was an error retrieving the SQL information.");
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
        return null;
    }
    // endregion
}