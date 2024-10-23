package com.example.fleursonthego.Models; // Update to your actual package structure

public class User {
    private String fullName;
    private String phoneNumber;
    private String userId;

    // Default constructor (required for Firestore)
    public User() {
    }

    // Constructor to initialize fields
    public User(String fullName, String phoneNumber, String userId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
