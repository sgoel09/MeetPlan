package com.example.meetplan.models;

import javax.annotation.Nullable;

public class User {

    private String username;

    public User(String username) {
        this.username = username;
    }

    @Nullable
    public String getUsername() {
        return username;
    }
}
