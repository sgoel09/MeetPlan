package com.example.meetplan.details;

import android.view.View;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/** Click listener to bring up the spinner dialog of users
 * to select and invite a user to the meetup. */
public class InviteClickListener implements View.OnClickListener {

    /** Spinner dialog to show. */
    private SpinnerDialog spinnerDialog;

    InviteClickListener(SpinnerDialog spinnerDialog) {
        this.spinnerDialog = spinnerDialog;
    }

    @Override
    public void onClick(View view) {
        spinnerDialog.showSpinerDialog();
    }
}
