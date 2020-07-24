package com.example.meetplan.expenses;

import android.view.View;

import com.example.meetplan.models.Meetup;

public class CalculateClickListener implements View.OnClickListener {

    private Meetup meetup;
    private ExpenseManager manager;


    CalculateClickListener(Meetup meetup) {
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        manager = new ExpenseManager(meetup);
    }
}
