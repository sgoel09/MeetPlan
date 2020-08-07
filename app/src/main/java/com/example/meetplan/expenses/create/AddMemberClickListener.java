package com.example.meetplan.expenses.create;

import android.view.View;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/** Click listener to bring up the spinner dialog of members
 * to select and add a member to the expense. */
public class AddMemberClickListener implements View.OnClickListener {

    /** Spinner dialog to show. */
    private SpinnerDialog spinnerDialog;

    AddMemberClickListener(SpinnerDialog spinnerDialog) {
        this.spinnerDialog = spinnerDialog;
    }

    @Override
    public void onClick(View view) {
        spinnerDialog.showSpinerDialog();
    }
}
