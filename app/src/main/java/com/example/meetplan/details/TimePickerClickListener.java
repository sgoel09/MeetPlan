package com.example.meetplan.details;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.meetplan.models.Meetup;

public class TimePickerClickListener implements View.OnClickListener {

    private Meetup meetup;
    private FragmentManager manager;

    TimePickerClickListener(FragmentManager manager, Meetup meetup) {
        this.meetup = meetup;
        this.manager = manager;
    }

    @Override
    public void onClick(View view) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(meetup);
        newFragment.show(manager, TimePickerFragment.class.getSimpleName());
    }
}
