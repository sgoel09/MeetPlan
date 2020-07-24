package com.example.meetplan.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

@ParseClassName("Expense")
public class Expense extends ParseObject {

    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERS = "users";

    public Expense() {}

    public Expense(String name, double amount, ArrayList<String> users) {
        put(KEY_AMOUNT, amount);
        put(KEY_NAME, name);
        put(KEY_USERS, users);
    }

    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get(KEY_USERS);
    }

    public double getAmount() {
        return getDouble(KEY_AMOUNT);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

}
