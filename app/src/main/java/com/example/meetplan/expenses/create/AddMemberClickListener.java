package com.example.meetplan.expenses.create;

import android.view.View;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddMemberClickListener implements View.OnClickListener {

    private SpinnerDialog spinnerDialog;

    AddMemberClickListener(SpinnerDialog spinnerDialog) {
        this.spinnerDialog = spinnerDialog;
    }

    @Override
    public void onClick(View view) {
        int i = 0;
        spinnerDialog.showSpinerDialog();
    }
}
