package com.example.Models;

public class User {
    private String userID;
    private String description;

    public User(String userID, String description) {
        this.userID = userID;
        this.description = description;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

}
