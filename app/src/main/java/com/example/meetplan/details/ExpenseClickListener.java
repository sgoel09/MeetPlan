package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.R;
import com.example.meetplan.expenses.ExpenseFragment;
import com.example.meetplan.models.Meetup;

/** Click listener for displaying the expense fragment of a meetup when
 * the expense button is clicked. */
public class ExpenseClickListener implements View.OnClickListener {

    /** Selected meetup for which to show expenses. */
    private Meetup meetup;

    /** Fragment manager of the details fragment. */
    private FragmentManager fragmentManager;

    public ExpenseClickListener(FragmentManager manager, Context context, Meetup meetup) {
        this.meetup = meetup;
        this.fragmentManager = manager;
    }

    /** On click, creates a new instance of the expense fragment and
     * begins transaction to it. */
    @Override
    public void onClick(View view) {
        ExpenseFragment fragment = ExpenseFragment.newInstance(meetup);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(ExpenseFragment.class.getSimpleName())
                .replace(R.id.flContainer, fragment)
                .commit();
    }
}
