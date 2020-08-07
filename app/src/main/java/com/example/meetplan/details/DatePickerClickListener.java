package com.example.meetplan.details;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.models.Meetup;

/** Click listener for the date button that creates a date picker
 * dialog fragment for the user to choose a date from a calendar. */
public class DatePickerClickListener implements View.OnClickListener {

    /** Selected meetup for which the date is being picked for. */
    private Meetup meetup;

    /** Fragment manager of the details fargment. */
    private FragmentManager manager;

    DatePickerClickListener(FragmentManager manager, Meetup meetup) {
        this.meetup = meetup;
        this.manager = manager;
    }

    /** On click, creates a new instance of the data picker fragment and shows it. */
    @Override
    public void onClick(View view) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(meetup);
        newFragment.show(manager, DatePickerFragment.class.getSimpleName());
    }
}
