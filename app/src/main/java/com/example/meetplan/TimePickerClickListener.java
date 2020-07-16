package com.example.meetplan;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TimePickerClickListener implements View.OnClickListener {

    private Meetup meetup;
    private AppCompatActivity activity;

    TimePickerClickListener(AppCompatActivity activity, Meetup meetup) {
        this.activity = activity;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(meetup);
        newFragment.show(activity.getSupportFragmentManager(), TimePickerFragment.class.getSimpleName());
    }
}
