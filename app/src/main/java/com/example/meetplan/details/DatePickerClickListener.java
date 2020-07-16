package com.example.meetplan.details;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.models.Meetup;

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
