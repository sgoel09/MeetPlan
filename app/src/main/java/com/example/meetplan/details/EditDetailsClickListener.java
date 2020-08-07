package com.example.meetplan.details;

import android.view.View;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;

/** Click listener for changing the mode of details fragment to edit
 * when the edit button is clicked. */
public class EditDetailsClickListener implements View.OnClickListener {

    /** Selected meetup for which the details are being edited. */
    private Meetup meetup;

    /** View binding of the details fragment. */
    private FragmentDetailsBinding binding;

    EditDetailsClickListener(FragmentDetailsBinding binding, Meetup meetup) {
        this.binding = binding;
        this.meetup = meetup;
    }

    /** Changes the mode to edit details, with all view labels and
     * edit buttons visible. */
    @Override
    public void onClick(View view) {
        DetailsFragment.changeMode(View.VISIBLE, meetup, true, binding);
    }
}
