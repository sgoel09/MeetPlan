package com.example.meetplan.details;

import android.view.View;

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
