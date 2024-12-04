package com.example.li_auto_shop;
public class User {
    private final String username, password, email, imagePath;
    private final int grade;
    
    public User(String username, String password,
                String email, int grade, String imagePath) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.grade = grade;
        this.imagePath = imagePath;
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
    
    public String getImagePath() {
        return imagePath;
    }
    // endregion
}