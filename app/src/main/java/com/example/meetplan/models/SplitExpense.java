package com.example.meetplan.models;

import android.util.Pair;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

@ParseClassName("SplitExpense")
public class SplitExpense extends ParseObject {

    private static final String KEY_SPLIT = "split";
    private static final String KEY_PAID = "paid";

    public SplitExpense() {}

    public SplitExpense(HashMap<String, Integer> splits, String paid) {
        put(KEY_SPLIT, splits);
        put(KEY_PAID, paid);
    }

    public HashMap<String, Integer> getSplit() {
        return (HashMap<String, Integer>) get(KEY_SPLIT);
    }

    public String getPaid() {
        return getString(KEY_PAID);
    }
}
