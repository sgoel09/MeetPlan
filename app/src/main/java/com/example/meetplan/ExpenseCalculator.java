package com.example.meetplan;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.parse.ParseUser;

import java.util.HashMap;

public class ExpenseCalculator {

    private static final String KEY_MEETUP = "meetup";
    private static final String TAG = "ExpenseCalculator";
    private Meetup meetup;
    private ImmutableList<Expense> allExpenses;
    private HashMap<ParseUser, Double> allNets;
    private ImmutableSortedMap<ParseUser, Double> allNetsImmutable;

    public ExpenseCalculator(Meetup meetup) {
        this.meetup = meetup;
    }
}
