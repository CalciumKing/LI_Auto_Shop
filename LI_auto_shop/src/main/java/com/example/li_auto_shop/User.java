package com.example.li_auto_shop;
public class User {
    private final String username, password, email;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
}