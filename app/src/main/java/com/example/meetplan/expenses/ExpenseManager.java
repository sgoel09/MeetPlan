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

/** Manager that handles the expenses of an expense.
 * Calculates the net totals for each member and calculates the optimal transactions
 * between members of a meetup. */
public class ExpenseManager {

    /** Double number to divide from when rounding to the hundreths. */
    private static final double ROUND_DOUBLE = 100.0;

    /** ImmutableList of all expenses in the meetup. */
    private ImmutableList<Expense> allExpenses;

    /** Hashmap that maps a user to their net total in the entire meetup. */
    private HashMap<String, Double> allNets;

    /** ImmutableSortedMap that maps a user to their net total in the the entire meetup. */
    private ImmutableSortedMap<String, Double> allNetsImmutable;

    /** ImmutableList of all transactions in the meetup. */
    private ImmutableList<Transaction> allTransactions;

    /** Transaction adapter for the recyclerview of transactions. */
    private TransactionAdapter transactionAdapter;

    public ExpenseManager(TransactionAdapter adapter, ImmutableList<Expense> expenses) {
        this.transactionAdapter = adapter;
        allExpenses = expenses;
    }

    /** Calculates the net total for all members part of at least one expense in the meetup.
     * Then, proceeds to find the optimal transactions. */
    protected void calculateNetTotals() {
        allNets = new HashMap<>();
        for (Expense expense : allExpenses) {
            eachExpense(expense, expense.getSplitExpense());
        }
        findTransactions();
    }

    /** Finds the next optimal transaction until no users have a nonzero net total. */
    private void findTransactions() {
        allTransactions = ImmutableList.of();
        while (allNets.size() > 1) {
            makeTransaction();
        }
    }

    /** Determines the total for each member in the expense and proceeds to update their net total. */
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

    /** Updates the specified user's net total by adding their total from the last expense. */
    private void updateAllNets(String user, double personalTotal) {
        if (allNets.containsKey(user)) {
            allNets.put(user, personalTotal + allNets.get(user));
        } else {
            allNets.put(user, personalTotal);
        }
    }

    /** Calcualtes the personal total of a user from an expense, based on the number of people they are
     * paying on behalf of and whether they are the payer of the expense. */
    private double calculatePersonalTotal(String user, Map<String, Integer> splits, String paid, int share, int total, double amount) {
        if (user.equals(paid)) {
            return amount - calculatePersonalExpenseTotal(share, total, amount);
        } else {
            return -calculatePersonalExpenseTotal(share, total, amount);
        }
    }

    /** Calculates the amount a user owes for one expense, based on the number of people they are
     * paying on behalf of and the total of the expense. */
    private Double calculatePersonalExpenseTotal(int share, int total, double amount) {
        return (amount/total) * share;
    }

    /** Calculate an optimal transaction by choosing the members that have the highest and
     * lowest net total. Creates the transaction and updates the adapter with the new transaction. */
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

    /** Sorts the ImmutableSortedMap of net totals from least to greatest. */
    private void sortMap() {
        Ordering naturalOrdering = Ordering.natural().onResultOf(Functions.forMap(allNets, null)).compound(Ordering.arbitrary());
        allNetsImmutable = ImmutableSortedMap.copyOf(allNets, naturalOrdering);
    }
}
