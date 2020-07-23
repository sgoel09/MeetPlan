package com.example.meetplan;

import android.util.Log;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.parse.ParseUser;

import java.util.HashMap;

import static java.lang.Math.abs;

public class ExpenseCalculator {

    private static final String KEY_MEETUP = "meetup";
    private static final String TAG = "ExpenseCalculator";
    private Meetup meetup;
    private ImmutableList<Expense> allExpenses;
    private HashMap<String, Double> allNets;
    private ImmutableSortedMap<String, Double> allNetsImmutable;

    public ExpenseCalculator() {
    }

    private void calculateTransactions() {
        Ordering ordering = Ordering.natural().onResultOf(Functions.forMap(allNets));
        allNetsImmutable = ImmutableSortedMap.copyOf(allNets, ordering);
        while (allNetsImmutable.size() > 1) {
            makeTransaction();
        }
    }

    private void makeTransaction() {
        double transactionAmount;
        String debtor = allNetsImmutable.firstKey();
        String creditor = allNetsImmutable.lastKey();
        if (allNetsImmutable.get(creditor) > abs(allNetsImmutable.get(debtor))) {
            transactionAmount = abs(allNetsImmutable.get(debtor));
            allNets.put(creditor, allNetsImmutable.get(creditor) - transactionAmount);
            allNets.remove(debtor);
            Ordering ordering = Ordering.natural().onResultOf(Functions.forMap(allNets));
            allNetsImmutable = ImmutableSortedMap.copyOf(allNets, ordering);
        } else {
            transactionAmount = allNetsImmutable.get(creditor);
            allNets.put(debtor, allNetsImmutable.get(debtor) + transactionAmount);
            allNets.remove(creditor);
            Ordering ordering = Ordering.natural().onResultOf(Functions.forMap(allNets));
            allNetsImmutable = ImmutableSortedMap.copyOf(allNets, ordering);
        }
        Log.i(TAG, debtor + " pays " + creditor + " $" + transactionAmount);
    }
}
