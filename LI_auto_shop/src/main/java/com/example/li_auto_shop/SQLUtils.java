package com.example.li_auto_shop;

import javafx.scene.control.Alert;

import java.sql.*;

public class SQLUtils {
    // region Main Methods
    public static void Login(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?";
        RunSQL(sql, username, password, email, true);
    }
    public static void Register(String username, String password, String email) {
        String sql = "insert into users_table (username, password, email) values (?, ?, ?)";
        RunSQL(sql, username, password, email, false);
    }
    public static void ResetPassword(String username, String newPassword, String email) {
        String sql = "update users_table set password=? where username=? and email=?;";
        RunSQL(sql, newPassword, username, email, false);
    }
    // endregion
    // region Utils
    private static Connection ConnectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/login_and_register", "root", "password");
        } catch (Exception ignored) {
            Utils.ErrorAlert(Alert.AlertType.ERROR, "Connection Error", "Error Connecting To Database", "Database could not be connected to, please try again.");
        }
        return null;
    }
    public static boolean ValidInfo(String username, String password, String email) {
        String sql = "select * from users_table where username = ? and password = ? and email = ?";
        Connection connect = ConnectDB();
        if (connect == null)
            return false;
        
        try (PreparedStatement prepared = connect.prepareStatement(sql)) {
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, email);
            ResultSet result = prepared.executeQuery();
            System.out.println("working");
            // FORM ALWAYS RESULTS IN WORKING, EVEN WHEN USER IS INVALID, DOES NOT ADD TO TABLE THO
            
            if(result.next()) {
                DashboardController.WelcomeName(result.getString("username"));
                return true;
            } else {
                System.out.println("not working");
                return false;
            }
        } catch (Exception ignored) {
            Utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
        System.out.println("not working");
        return false;
    }
    private static void RunSQL(String sql, String username, String password, String email, boolean query) {
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
            Utils.ErrorAlert(Alert.AlertType.ERROR, "SQL Error", "Error Retrieving SQL Information, from RUNSQL", "There was an error retrieving the SQL information.");
        } catch (Exception ignored) {
            Utils.ErrorAlert(Alert.AlertType.ERROR, "Error", "Error Running SQL", "There was an error running the SQL information, or that user doesn't exist.");
        }
    }
    // endregion
}