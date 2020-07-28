package com.example.meetplan.browse.addtask;

import android.view.View;

public class CloseClickListener implements View.OnClickListener {

    private FragmentDismisser dismisser;

    public CloseClickListener(FragmentDismisser dismisser) {
        this.dismisser = dismisser;
    }

    @Override
    public void onClick(View view) {
        dismisser.dismissFragment();
    }
}
