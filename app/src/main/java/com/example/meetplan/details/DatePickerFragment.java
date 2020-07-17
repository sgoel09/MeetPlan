package com.example.meetplan.details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.models.Meetup;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Meetup meetup;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Meetup input) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", input);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return  dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date date = c.getTime();
        meetup.setDate(date);
        meetup.saveInBackground();
        Fragment fragment = DetailsFragment.newInstance(meetup);
        ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}