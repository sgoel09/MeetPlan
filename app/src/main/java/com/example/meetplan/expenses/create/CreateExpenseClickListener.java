package com.example.meetplan.expenses.create;

import android.view.View;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.expenses.PassExpense;
import com.example.meetplan.expenses.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.expenses.models.SplitExpense;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Map;

/** Click listener to create an expense based on the inputted information
 * and save the expense to the selected meetup for which expenses are shown. */
public class CreateExpenseClickListener implements View.OnClickListener {

    /** View binding for the create expense fragment. */
    private final FragmentCreateExpenseBinding binding;

    /** The create expense fragment. */
    private final CreateExpenseFragment fragment;

    /** Selected meetup for which an expense is being made. */
    private Meetup meetup;

    /** Map that holds the association between each user and the number they are paying on behalf for. */
    private Map<String, Integer> splits;

    public CreateExpenseClickListener(FragmentCreateExpenseBinding binding, CreateExpenseFragment fragment, Meetup meetup, Map<String, Integer> splits) {
        this.binding = binding;
        this.fragment = fragment;
        this.meetup = meetup;
        this.splits = splits;
    }

    /** Retrieves the information from views, creates an expense, and saves the expense to the meetup.
     * Passes the expense to the expense fragment to display the newly created expense in the recyclerview
     * with the rest of the expenses. */
    @Override
    public void onClick(View view) {
        String name = binding.name.getText().toString();
        String amount = binding.amount.getText().toString();
        SplitExpense splitExpense = new SplitExpense(splits, ParseUser.getCurrentUser().getUsername());
        splitExpense.saveInBackground();
        Expense expense = new Expense(name, amount, splitExpense);
        expense.saveInBackground();
        ArrayList<Expense> expenses = meetup.getExpenses();
        expenses.add(expense);
        meetup.setExpenses(expenses);
        meetup.saveInBackground();
        PassExpense mHost = (PassExpense) fragment.getTargetFragment();
        mHost.passNewExpense(expense);
        fragment.dismiss();
    }
}
