package com.example.meetplan.expenses;

import android.util.Log;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.SplitExpense;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class ExpenseManager {

    private static final String KEY_MEETUP = "meetup";
    private static final String TAG = "ExpenseCalculator";
    private Meetup meetup;
    private ImmutableList<Expense> allExpenses;
    private HashMap<String, Double> allNets;
    private ImmutableSortedMap<String, Double> allNetsImmutable;

    public ExpenseManager(Meetup meetup) {
        this.meetup = meetup;
        queryExpenses(meetup);
    }

    public void calculateTransactions() {
        calculateNetTotals();
        while (allNets.size() > 1) {
            makeTransaction();
        }
    }

    private void calculateNetTotals() {
        allNets = new HashMap<>();
        for (Expense expense : allExpenses) {
            querySplitExpense(expense);
        }
    }

    private void eachExpense(Expense expense, SplitExpense splitExpense) {
        double amount = expense.getAmount();
        int total = 0;
        String paid = splitExpense.getPaid();
        for (Map.Entry<String, Integer> entry : splitExpense.getSplit().entrySet()) {
            total += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : splitExpense.getSplit().entrySet()) {
            String user = entry.getKey();
            int share = entry.getValue();
            double personalTotal = 0;
            if (user.equals(paid)) {
                personalTotal = amount;
            } else {
                personalTotal = -calculatePersonalTotal(share, total, amount);
            }
            if (allNets.containsKey(user)) {
                allNets.put(user, personalTotal + allNets.get(user));
            } else {
                allNets.put(user, personalTotal);
            }
        }
    }

    private Double calculatePersonalTotal(int share, int total, double amount) {
        return (amount/total) * share;
    }

    private void makeTransaction() {
        double transactionAmount;
        sortMap();
        String debtor = allNetsImmutable.firstKey();
        String creditor = allNetsImmutable.lastKey();
        if (allNetsImmutable.get(creditor) > abs(allNetsImmutable.get(debtor))) {
            transactionAmount = abs(allNetsImmutable.get(debtor));
            allNets.put(creditor, allNetsImmutable.get(creditor) - transactionAmount);
            allNets.remove(debtor);
        } else {
            transactionAmount = allNetsImmutable.get(creditor);
            allNets.put(debtor, allNetsImmutable.get(debtor) + transactionAmount);
            allNets.remove(creditor);
        }
        sortMap();
        Log.i(TAG, debtor + " pays " + creditor + " $" + transactionAmount);
    }

    private void sortMap() {
        Ordering naturalOrdering = Ordering.natural().onResultOf(Functions.forMap(allNets, null));
        allNetsImmutable = ImmutableSortedMap.copyOf(allNets, naturalOrdering);
    }

    private void queryExpenses(final Meetup meetup) {
        final List<Expense>[] expenses = new List[]{null};
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereEqualTo("objectId", meetup.getObjectId());
        query.setLimit(1);
        query.include("expenses");
        query.findInBackground(new FindCallback<Meetup>() {
            @Override
            public void done(List<Meetup> meetups, ParseException e) {
                expenses[0] = meetups.get(0).getExpenses();
                allExpenses = ImmutableList.<Expense>builder().addAll(expenses[0]).build();
                calculateTransactions();
            }
        });
    }

    private void querySplitExpense(final Expense expense) {
        final SplitExpense[] newExpence = {new SplitExpense()};
        ParseQuery<Expense> query = ParseQuery.getQuery(Expense.class);
        query.whereEqualTo("objectId", expense.getObjectId());
        query.setLimit(1);
        query.include("splitexpense");
        query.findInBackground(new FindCallback<Expense>() {
            @Override
            public void done(List<Expense> expenses, ParseException e) {
                newExpence[0] = expenses.get(0).getSplitExpense();
                Log.i("QuerySplitExpense", newExpence[0].toString());
                eachExpense(expense, newExpence[0]);
            }
        });
    }
}
