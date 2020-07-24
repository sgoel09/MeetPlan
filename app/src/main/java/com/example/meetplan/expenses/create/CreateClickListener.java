package com.example.meetplan.expenses.create;

import android.view.View;

import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;

import java.util.ArrayList;

public class CreateClickListener implements View.OnClickListener {

    private CreateExpenseFragment fragment;
    private String name;
    private double amount;
    private Meetup meetup;

    public CreateClickListener(CreateExpenseFragment fragment, Meetup meetup, String name, String amountString) {
        this.fragment = fragment;
        this.name = name;
        this.amount = Double.parseDouble(amountString);
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {

    }
}
