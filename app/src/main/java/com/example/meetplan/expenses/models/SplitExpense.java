package com.example.meetplan.expenses.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Model data class for the split in expense.
 * Creates a new data point in the Parse database with the given information
 * on initialization of the object.
 * */
@ParseClassName("SplitExpense")
public class SplitExpense extends ParseObject {

    /** Key for the split of the expense in the Parse database. */
    private static final String KEY_SPLIT = "split";

    /** Key for the user who paid for the expense in the Parse database. */
    private static final String KEY_PAID = "paid";

    /** Required empty public constructor. */
    public SplitExpense() {}

    /** Creates an puts the mapping of users to their share of their expense,
     * and the payer of the expense in the database. */
    @Nullable
    public SplitExpense(Map<String, Integer> splits, String paid) {
        put(KEY_SPLIT, splits);
        put(KEY_PAID, paid);
    }

    /** @return how the expense is split between users */
    @Nullable
    public HashMap<String, Integer> getSplit() {
        return (HashMap<String, Integer>) get(KEY_SPLIT);
    }

    /** @return payer of the split expense */
    @Nullable
    public String getPaid() {
        return getString(KEY_PAID);
    }
}
