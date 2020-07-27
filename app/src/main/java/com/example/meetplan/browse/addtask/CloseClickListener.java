package com.example.meetplan.browse.addtask;

import android.view.View;

public class CloseClickListener implements View.OnClickListener {

    private AddTaskFragment fragment;

    public CloseClickListener(AddTaskFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View view) {
        fragment.dismiss();
    }
}
