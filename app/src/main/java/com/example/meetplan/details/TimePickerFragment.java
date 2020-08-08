package com.example.meetplan.details;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.models.Meetup;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment for the TimePickerDialog fragment to display a clock and input option for the
 * user to choose a time. Saves the time to the selected meetup after one is picked.
 * */
public class TimePickerFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    /** Selected meetup for which the date is being picked for. */
    Meetup meetup;

    /** Required empty constructor. */
    public TimePickerFragment() {}

    public static TimePickerFragment newInstance(Meetup input) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", input);
        fragment.setArguments(args);
        return fragment;
    }

    /** Set the selected time on creation of the time picker dialog fragment to
     * be the current time. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), R.style.PickerTheme, this, hour, minute, false);
        return dialog;
    }

    /** Saves the time when one is selected and goes back to the details fragment, this time
     * with the newly selected time displayed. */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Date date = meetup.getDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        date = calendar.getTime();
        meetup.setDate(date);
        final Context context = getContext();
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dismiss();
                Fragment fragment = DetailsFragment.newInstance(meetup, true);
                ((MainActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(DetailsFragment.class.getSimpleName())
                        .replace(R.id.flContainer, fragment)
                        .commit();
            }
        });
    }
}
