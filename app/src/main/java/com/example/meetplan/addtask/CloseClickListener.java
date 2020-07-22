package com.example.meetplan.events;

import android.view.View;

import com.example.meetplan.AddTaskFragment;

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
