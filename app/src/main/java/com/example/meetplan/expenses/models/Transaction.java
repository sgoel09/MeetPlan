package com.example.meetplan.expenses.models;

/**
 * Model data class for a transaction between two users
 * Holds the creditor (receives money), debtor (owes money),
 * and the amount of the transaction.
 * */
public class Transaction {

    /** The user that receives the money in the transaction. */
    private String creditor;

    /** The user that owes the money in the transaction. */
    private String debtor;

    /** The amount of the transaction. */
    private double amount;

    public Transaction(String creditor, String debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

    /** @return the creditor of the transaction */
    public String getCreditor() {
        return creditor;
    }

    /** @return the debtor of the transaction */
    public String getDebtor() {
        return debtor;
    }

    /** @return the amount of the transaction */
    public double getAmount() {
        return amount;
    }
}
