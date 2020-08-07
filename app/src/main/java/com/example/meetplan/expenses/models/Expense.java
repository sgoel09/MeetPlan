package com.example.meetplan.expenses.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Model data class for an expense.
 * Creates a new data point in the Parse database with the given information
 * on initialization of the object.
 * */
@ParseClassName("Expense")
public class Expense extends ParseObject {

    /** Key for the amount of the expense in the Parse database. */
    public static final String KEY_AMOUNT = "amount";

    /** Key for the name of the expense in the Parse database. */
    public static final String KEY_NAME = "name";

    /** Key for the users of the expense in the Parse database. */
    public static final String KEY_USERS = "users";

    /** Key for the pointer to the SplitExpense of the expense in the Parse database. */
    public static final String KEY_SPLIT_EXPENSE = "splitexpense";

    /** Required empty public constructor. */
    public Expense() {}

    /** Creates an puts the name, amount, and SplitExpense of the new expense
     * in the database. */
    public Expense(String name, String amount, SplitExpense split) {
        put(KEY_AMOUNT, amount);
        put(KEY_NAME, name);
        put(KEY_SPLIT_EXPENSE, split);
    }

    /** @return users of the expense */
    @Nullable
    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get(KEY_USERS);
    }

    /** @return amount of the expense */
    @Nullable
    public double getAmount() {
        return Double.parseDouble(getString(KEY_AMOUNT));
    }

    /** @return name of the expense */
    @Nullable
    public String getName() {
        return getString(KEY_NAME);
    }

    /** @return SplitExpense of the expense */
    @Nullable
    public SplitExpense getSplitExpense() {
        return (SplitExpense) get(KEY_SPLIT_EXPENSE);
    }
}
