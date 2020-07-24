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
    public static final String KEY_SPLIT_EXPENSE = "splitexpense";

    public Expense() {}

    public Expense(String name, String amount, SplitExpense split) {
        put(KEY_AMOUNT, amount);
        put(KEY_NAME, name);
        put(KEY_SPLIT_EXPENSE, split);
    }

    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get(KEY_USERS);
    }

    public double getAmount() {
        return Double.parseDouble(getString(KEY_AMOUNT));
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public SplitExpense getSplitExpense() {
        return (SplitExpense) get(KEY_SPLIT_EXPENSE);
    }

}
