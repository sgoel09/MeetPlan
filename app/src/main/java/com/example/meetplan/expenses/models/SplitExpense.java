package com.example.meetplan.expenses.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

@ParseClassName("SplitExpense")
public class SplitExpense extends ParseObject {

    private static final String KEY_SPLIT = "split";
    private static final String KEY_PAID = "paid";

    public SplitExpense() {}

    @Nullable
    public SplitExpense(Map<String, Integer> splits, String paid) {
        put(KEY_SPLIT, splits);
        put(KEY_PAID, paid);
    }

    @Nullable
    public HashMap<String, Integer> getSplit() {
        return (HashMap<String, Integer>) get(KEY_SPLIT);
    }

    @Nullable
    public String getPaid() {
        return getString(KEY_PAID);
    }
}
