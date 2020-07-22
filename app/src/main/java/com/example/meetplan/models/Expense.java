package com.example.meetplan.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

@ParseClassName("Expense")
public class Expense extends ParseObject {

    public static final String KEY_MEETUP = "meetup";
    public static final String KEY_PAYER = "payer";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_SHARES = "shares";

    public Expense() {}

    public Meetup getMeetup() {
        return (Meetup) getParseObject(KEY_MEETUP);
    }

    public void setMeetup(Meetup meet) {
        put(KEY_MEETUP, meet);
    }

    public ParseUser getPayer() {
        return getParseUser(KEY_PAYER);
    }

    public void setPayer(String payer) {
        put(KEY_PAYER, payer);
    }

    public double getAmount() {
        return getDouble(KEY_AMOUNT);
    }

    public void setAmount(double amount) {
        put(KEY_AMOUNT, amount);
    }

    public ArrayList<HashMap<ParseUser, Integer>> getShares() {
        return (ArrayList<HashMap<ParseUser, Integer>>) get(KEY_SHARES);
    }

    public void setShares(ArrayList<HashMap<ParseUser, Integer>> shares) {
        put(KEY_SHARES, shares);
    }

}
