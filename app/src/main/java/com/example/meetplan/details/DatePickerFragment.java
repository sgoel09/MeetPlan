package com.example.meetplan.details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

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
 * Fragment for the DatePickerDialog fragment to display a calendar for the
 * user to choose a date. Saves the date to the selected meetup after one is picked.
 * */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /** Selected meetup for which the date is being picked for. */
    private Meetup meetup;

    /** Required empty constructor. */
    public DatePickerFragment() {}

    public static DatePickerFragment newInstance(Meetup input) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", input);
        fragment.setArguments(args);
        return fragment;
    }

    /** Set the selected date on creation of the date picker dialog fragment to
     * be the current date. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.PickerTheme, this, year, month, day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return  dialog;
    }

    /** Saves the date when one is selected and goes back to the details fragment, this time
     * with the newly selected date displayed. */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date date = c.getTime();
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
