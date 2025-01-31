package com.example.li_auto_shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class SQLUtils {
    // region Login/Signup
    public static User login(String username, String password) {
        String sql = "select * from users_table where username = ? and password = ? limit 1;";
        return runFormSQL(sql, username, password, null, true);
    }
    
    public static User register(String username, String password, String email) {
        String sql = "insert into users_table (username, password, email) values (?, ?, ?);";
        return runFormSQL(sql, username, password, email, false);
    }
    
    public static void resetPassword(String username, String newPassword, String email) {
        String sql = "update users_table set password=? where username=? and email=?;";
        runFormSQL(sql, newPassword, username, email, false);
    }
    // endregion
    
    // region Table
    public static void addItem(String id, String brand, String model, double price, int on_hand, int reorder_level, String path) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "insert into items (id, brand, model_number, price, on_hand, reorder_level, image) values (?, ?, ?, ?, ?, ?, ?);";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.setString(2, brand);
            prepared.setString(3, model);
            prepared.setDouble(4, price);
            prepared.setInt(5, on_hand);
            prepared.setInt(6, reorder_level);
            prepared.setString(7, path);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error In addItem", "There was an error running the SQL information to add to the table.");
        }
    }
    
    public static void updateItem(String id, String brand, String model, double price, int on_hand, int reorder_level, String path) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "update items set id = ?, brand = ?, model_number = ?, price = ?, on_hand = ?, reorder_level = ?, image = ? where id = ?;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.setString(2, brand);
            prepared.setString(3, model);
            prepared.setDouble(4, price);
            prepared.setInt(5, on_hand);
            prepared.setInt(6, reorder_level);
            if (path != null)
                prepared.setString(7, path);
            else {
                Item item = getItem(id);
                if (item == null) return;
                prepared.setString(7, item.getPath());
            }
            prepared.setString(8, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error In updateItem", "There was an error running the SQL information to update the table.");
        }
    }
    
    public static void deleteItem(String id) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "delete from items where id = ?;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error in deleteItem", "There was an error deleting information from the table.");
        }
    }
    
    public static ObservableList<Item> refreshTable() {
        Connection connection = connectDB();
        if (connection == null) return null;
        
        String sql = "select * from items;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            ResultSet result = prepared.executeQuery();
            ObservableList<Item> data = FXCollections.observableArrayList();
            
            while (result.next())
                data.add(new Item(
                        result.getString("id"),
                        result.getString("brand"),
                        result.getString("model_number"),
                        result.getDouble("price"),
                        result.getInt("on_hand"),
                        result.getInt("reorder_level"),
                        result.getString("image")));
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
    
    public static void addTransaction(Date date, String username) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "insert into transactions (date, username) values (?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setDate(1, date);
            prepared.setString(2, username);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error in addTransaction", "Error Adding Transaction To Database", "There was an error adding a transaction to the database, please try again.");
        }
    }
    
    public static void addAdjustment(String item_ID, int adjustment) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "insert into item_adjust (trans_ID, item_ID, adjustment) values ((select max(trans_ID) from transactions), ?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, item_ID);
            prepared.setInt(2, adjustment);
            prepared.executeUpdate();
        } catch (Exception e) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error in addAdjustment", "Error Adding Item Adjustment To Database", "There was an error adding an item adjustment to the database, please try again.");
        }
    }
    // endregion
    
    // region User Table
    public static void addUser(String username, String email, String password, int grade) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "insert into users_table (username, email, password, grade) values (?, ?, ?, ?);";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, email);
            prepared.setString(3, password);
            prepared.setInt(4, grade);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error In addUser", "There was an error running the SQL information to add a user to the table.");
        }
    }
    
    public static void updateUser(String username, String email, String password, int grade) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "update users_table set username = ?, email = ?, password = ?, grade = ? where username = ? limit 1;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, email);
            prepared.setString(3, password);
            prepared.setInt(4, grade);
            prepared.setString(5, username);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error in updateUser", "There was an error running the SQL information to update a user in the table.");
        }
    }
    
    public static void deleteUser(String username) {
        Connection connection = connectDB();
        if (connection == null) return;
        
        String sql = "delete from users_table where username = ?;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.executeUpdate();
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error in deleteUser", "There was an error deleting information from the user table.");
        }
    }
    
    public static ObservableList<User> refreshUserTable() {
        Connection connection = connectDB();
        if (connection == null) return null;
        
        String sql = "select * from users_table;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            ResultSet result = prepared.executeQuery();
            ObservableList<User> data = FXCollections.observableArrayList();
            
            while (result.next())
                data.add(new User(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getInt("grade"),
                        result.getString("image_path")));
            return data;
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error", "Error Refreshing Table", "There was an error running the SQL information to refresh the user table.");
            return null;
        }
    }
    
    public static User getUser(String username) {
        Connection connection = connectDB();
        if (connection == null) return null;
        
        String sql = "select * from users_table where username = ? limit 1;";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            ResultSet result = prepared.executeQuery();
            
            if (result.next())
                return new User(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getString(5)
                );
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error In getUser", "Error Getting User From Database", "There was an error getting a user from the database, please try again.");
        }
        return null;
    }
    
    public static ObservableList<AdjustedItem> getTransactions(String username) {
        Connection connection = connectDB();
        if (connection == null) return null;
        
        String sql = "select * from item_adjust where trans_id in (select trans_ID from transactions where username=?);";
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            
            ResultSet result = prepared.executeQuery();
            ObservableList<AdjustedItem> data = FXCollections.observableArrayList();
            
            while (result.next())
                data.add(new AdjustedItem(
                        result.getInt("trans_ID"),
                        result.getInt("adjustment"),
                        result.getString("item_ID")
                ));
            return data;
        } catch (Exception e) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error in getTransaction", "Error Getting Transactions From Database", "There was an error getting transactions from the database, please try again.");
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
        Connection connection = connectDB();
        if (connection == null)
            return null;
        
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            if (email != null)
                prepared.setString(3, email);
            
            if (query) {
                ResultSet result = prepared.executeQuery();
                
                if (result.next())
                    return new User(
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("email"),
                            result.getInt("grade"),
                            result.getString("image_path"));
            } else {
                prepared.executeUpdate();
                sql = "select * from users_table where username = ? and password = ? and email = ? limit 1;";
                return runFormSQL(sql, username, password, email, true); // gets the user from the database after creating it, only does recursion once
            }
        } catch (Exception ignored) {
            Utils.errorAlert(Alert.AlertType.ERROR, "Error in runFormSQL", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
        return null;
    }
    // endregion
}