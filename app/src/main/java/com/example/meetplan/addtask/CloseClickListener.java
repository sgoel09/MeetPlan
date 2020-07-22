package com.example.meetplan.addtask;

import android.view.View;

import com.example.meetplan.addtask.AddTaskFragment;

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
