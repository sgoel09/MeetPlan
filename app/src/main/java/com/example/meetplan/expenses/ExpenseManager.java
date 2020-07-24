package com.example.meetplan.expenses;

import android.util.Log;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.SplitExpense;
import com.example.meetplan.models.Transaction;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.toRadians;

public class ExpenseManager {

    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_SPLIT_EXPENSE = "splitexpense";
    private static final String KEY_EXPENSES = "expenses";
    private static final double ROUND_DOUBLE = 100.0;
    private static final int UNIQUE_LIMIT = 1;
    private static final String TAG = "ExpenseManager";
    private Meetup meetup;
    private ImmutableList<Expense> allExpenses;
    private HashMap<String, Double> allNets;
    private ImmutableSortedMap<String, Double> allNetsImmutable;
    private ImmutableList<Transaction> allTransactions;
    private TransactionAdapter transactionAdapter;
    private int numQueried = 0;

    public ExpenseManager(Meetup meetup, TransactionAdapter adapter, ImmutableList<Expense> expenses) {
        this.meetup = meetup;
        this.transactionAdapter = adapter;
        allExpenses = expenses;
    }

    public ImmutableList<Transaction> getTransactions() {
        return allTransactions;
    }

    private void calculateTransactions() {
        allTransactions = ImmutableList.of();
        while (allNets.size() > 1) {
            makeTransaction();
        }
    }

    protected void calculateNetTotals() {
        allNets = new HashMap<>();
        for (Expense expense : allExpenses) {
            eachExpense(expense, expense.getSplitExpense());
        }
        calculateTransactions();
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
                personalTotal = amount - calculatePersonalTotal(share, total, amount);
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
        transactionAmount = Math.round(transactionAmount * ROUND_DOUBLE) / ROUND_DOUBLE;
        Transaction transaction = new Transaction(creditor, debtor, transactionAmount);
        allTransactions = ImmutableList.<Transaction>builder().addAll(allTransactions).add(transaction).build();
        transactionAdapter.updateData(allTransactions);
    }

    private void sortMap() {
        Ordering naturalOrdering = Ordering.natural().onResultOf(Functions.forMap(allNets, null)).compound(Ordering.arbitrary());
        allNetsImmutable = ImmutableSortedMap.copyOf(allNets, naturalOrdering);
    }

    private void queryExpenses(final Meetup meetup) {
        final List<Expense>[] expenses = new List[]{null};
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereEqualTo(KEY_OBJECT_ID, meetup.getObjectId());
        query.setLimit(UNIQUE_LIMIT);
        query.include(KEY_EXPENSES);
        query.findInBackground(new FindCallback<Meetup>() {
            @Override
            public void done(List<Meetup> meetups, ParseException e) {
                expenses[0] = meetups.get(0).getExpenses();
                allExpenses = ImmutableList.<Expense>builder().addAll(expenses[0]).build();
                calculateNetTotals();
            }
        });
    }

    private void querySplitExpense(final Expense expense) {
        final SplitExpense[] newExpence = {new SplitExpense()};
        ParseQuery<Expense> query = ParseQuery.getQuery(Expense.class);
        query.whereEqualTo(KEY_OBJECT_ID, expense.getObjectId());
        query.setLimit(UNIQUE_LIMIT);
        query.include(KEY_SPLIT_EXPENSE);
        query.findInBackground(new FindCallback<Expense>() {
            @Override
            public void done(List<Expense> expenses, ParseException e) {
                newExpence[0] = expenses.get(0).getSplitExpense();
                numQueried += 1;
                eachExpense(expense, newExpence[0]);
            }
        });
    }
}
