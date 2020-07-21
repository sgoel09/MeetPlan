package com.example.meetplan.events;

import android.view.View;

public class CloseClickListener implements View.OnClickListener {

    private AddEventFragment fragment;

    public CloseClickListener(AddEventFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View view) {
        fragment.dismiss();
    }
}
