package com.example.meetplan.browse.addtask;

import android.view.View;

/** Listener for the close button in the AddTaskFragment.
 * Closes the dialog fragment.
 * */
public class CloseClickListener implements View.OnClickListener {

    /** Interface to dismiss the fragment. */
    private FragmentDismisser dismisser;

    /**
     * Constructor that saves the interface.
     * @param dismisser dismisses the fragment when its method is called
     * */
    public CloseClickListener(FragmentDismisser dismisser) {
        this.dismisser = dismisser;
    }

    @Override
    public void onClick(View view) {
        dismisser.dismissFragment();
    }
}
