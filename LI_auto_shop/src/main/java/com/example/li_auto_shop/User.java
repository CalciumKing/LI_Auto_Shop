package com.example.li_auto_shop;
public class User {
    private final String username, password, email;
    private final int grade;
    
    public User(String username, String password, String email, int grade) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.grade = grade;
    }
    
    // region Getters
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getGrade() {
        return grade;
    }
    // endregion
}