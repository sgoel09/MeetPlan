package com.example.meetplan.details;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class InviteClickListener implements View.OnClickListener {

    private static final String TAG = "InviteClickListener";
    private SpinnerDialog spinnerDialog;

    InviteClickListener(SpinnerDialog spinnerDialog) {
        this.spinnerDialog = spinnerDialog;
    }

    @Override
    public void onClick(View view) {
        spinnerDialog.showSpinerDialog();
    }
}
