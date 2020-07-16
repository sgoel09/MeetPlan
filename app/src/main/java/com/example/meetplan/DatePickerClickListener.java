package com.example.meetplan;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DatePickerClickListener implements View.OnClickListener {

    private Meetup meetup;
    private AppCompatActivity activity;

    DatePickerClickListener(AppCompatActivity activity, Meetup meetup) {
        this.activity = activity;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(meetup);
        newFragment.show(activity.getSupportFragmentManager(), DatePickerFragment.class.getSimpleName());
    }
}
