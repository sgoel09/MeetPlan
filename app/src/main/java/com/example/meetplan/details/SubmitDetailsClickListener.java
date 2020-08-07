package com.example.meetplan.details;

import android.view.View;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.Snackbar;

/** Click listener to change the mode of the details fragment to
 * view once the submit button is clicked. */
public class SubmitDetailsClickListener implements View.OnClickListener {

    /** Meetup for which the details are being viewed. */
    private Meetup meetup;

    /** View binding of the details fragment. */
    private FragmentDetailsBinding binding;

    SubmitDetailsClickListener(FragmentDetailsBinding binding, Meetup meetup) {
        this.binding = binding;
        this.meetup = meetup;
    }

    /** Changes the mode of the details fragment to view and notifies the user. */
    @Override
    public void onClick(View view) {
        DetailsFragment.changeMode(View.GONE, meetup, false, binding);
        Snackbar.make(binding.getRoot(), "Changes saved", Snackbar.LENGTH_SHORT).show();
    }
}
