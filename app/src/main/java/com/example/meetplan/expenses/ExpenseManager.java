package com.example.meetplan.expenses;

import com.example.meetplan.expenses.models.Expense;
import com.example.meetplan.expenses.models.SplitExpense;
import com.example.meetplan.expenses.models.Transaction;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

public class ExpenseManager {

    private static final double ROUND_DOUBLE = 100.0;
    private ImmutableList<Expense> allExpenses;
    private HashMap<String, Double> allNets;
    private ImmutableSortedMap<String, Double> allNetsImmutable;
    private ImmutableList<Transaction> allTransactions;
    private TransactionAdapter transactionAdapter;

    public ExpenseManager(TransactionAdapter adapter, ImmutableList<Expense> expenses) {
        this.transactionAdapter = adapter;
        allExpenses = expenses;
    }

    protected void calculateNetTotals() {
        allNets = new HashMap<>();
        for (Expense expense : allExpenses) {
            eachExpense(expense, expense.getSplitExpense());
        }
        findTransactions();
    }

    private void findTransactions() {
        allTransactions = ImmutableList.of();
        while (allNets.size() > 1) {
            makeTransaction();
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
            double personalTotal = calculatePersonalTotal(user, splitExpense.getSplit(), paid, share, total, amount);
            updateAllNets(user, personalTotal);
        }
        if (!splitExpense.getSplit().containsKey(paid)) {
            updateAllNets(paid, amount);
        }

    }

    private void updateAllNets(String user, double personalTotal) {
        if (allNets.containsKey(user)) {
            allNets.put(user, personalTotal + allNets.get(user));
        } else {
            allNets.put(user, personalTotal);
        }
    }

    private double calculatePersonalTotal(String user, Map<String, Integer> splits, String paid, int share, int total, double amount) {
        if (user.equals(paid)) {
            return amount - calculatePersonalExpenseTotal(share, total, amount);
        } else {
            return -calculatePersonalExpenseTotal(share, total, amount);
        }
    }

    private Double calculatePersonalExpenseTotal(int share, int total, double amount) {
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
}
