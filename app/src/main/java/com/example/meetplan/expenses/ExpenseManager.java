package com.example.meetplan.expenses;

import android.util.Log;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import static java.lang.Math.abs;

public class ExpenseManager {

    private static final String KEY_MEETUP = "meetup";
    private static final String TAG = "ExpenseCalculator";
    private Meetup meetup;
    private ImmutableList<Expense> allExpenses;
    private SortedMap<String, Double> allNets;

    public ExpenseManager(Meetup meetup) {
        this.meetup = meetup;
        allExpenses = ImmutableList.<Expense>builder().addAll(meetup.getExpenses()).build();
    }

    private void calculateTransactions() {
        while (allNets.size() > 1) {
            makeTransaction();
        }
    }

    private void makeTransaction() {
        double transactionAmount;
        String debtor = allNets.firstKey();
        String creditor = allNets.lastKey();
        if (allNets.get(creditor) > abs(allNets.get(debtor))) {
            transactionAmount = abs(allNets.get(debtor));
            allNets.put(creditor, allNets.get(creditor) - transactionAmount);
            allNets.remove(debtor);
        } else {
            transactionAmount = allNets.get(creditor);
            allNets.put(debtor, allNets.get(debtor) + transactionAmount);
            allNets.remove(creditor);
        }
        Log.i(TAG, debtor + " pays " + creditor + " $" + transactionAmount);
    }
}
