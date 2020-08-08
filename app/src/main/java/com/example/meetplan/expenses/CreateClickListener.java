package com.example.meetplan.expenses;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.expenses.create.CreateExpenseFragment;
import com.example.meetplan.models.Meetup;

/** Click listener to make a create expense fragment to enter
 * information about a new expense. */
public class CreateClickListener implements View.OnClickListener {

    /** Context of the expense fragment. */
    private Context context;

    /** Selected meetup for which an expense will be created. */
    private Meetup meetup;

    /** Previous expense fragment of the meetup. */
    private ExpenseFragment expenseFragment;

    public CreateClickListener(Context context, Meetup meetup, ExpenseFragment fragment) {
        this.context = context;
        this.meetup = meetup;
        this.expenseFragment = fragment;
    }

    /** Shows the create expense dialog fragment when the create button is clicked. */
    @Override
    public void onClick(View view) {
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        CreateExpenseFragment fragment = CreateExpenseFragment.newInstance(meetup);
        fragment.setTargetFragment(expenseFragment, 0);
        fragment.show(fm, "tag");
    }
}
