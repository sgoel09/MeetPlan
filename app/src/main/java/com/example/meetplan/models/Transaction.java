package com.example.meetplan.models;

public class Transaction {

    private String creditor;
    private String debtor;
    private double amount;

    public Transaction(String creditor, String debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

    public String getCreditor() {
        return creditor;
    }

    public String getDebtor() {
        return debtor;
    }

    public double getAmount() {
        return amount;
    }
}
