package com.example.meetplan.details;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.models.Meetup;

/** Click listener for the time button that creates a time picker
 * dialog fragment for the user to choose a time from the clock or input. */
public class TimePickerClickListener implements View.OnClickListener {

    /** Selected meetup for which the time is being picked for. */
    private Meetup meetup;

    /** Fragment manager of the details fargment. */
    private FragmentManager manager;

    TimePickerClickListener(FragmentManager manager, Meetup meetup) {
        this.meetup = meetup;
        this.manager = manager;
    }

    /** On click, creates a new instance of the time picker fragment and shows it. */
    @Override
    public void onClick(View view) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(meetup);
        newFragment.show(manager, TimePickerFragment.class.getSimpleName());
    }
}
