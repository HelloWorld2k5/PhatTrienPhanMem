package com.coffeeshop.model;

// Người dùng
public class User {
    private int userId;
    private String username;
    private String encryptedPassword;
    private String fullName;
    private UserRole userRole;
    private UserStatus userStatus;

    public User() {}

    public User(int userId, String username, String encryptedPassword, String fullName, UserRole userRole, UserStatus userStatus) {
        this.userId = userId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.fullName = fullName;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}