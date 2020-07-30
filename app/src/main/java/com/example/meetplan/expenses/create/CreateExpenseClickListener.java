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

public class CreateExpenseClickListener implements View.OnClickListener {

    private final FragmentCreateExpenseBinding binding;
    private final CreateExpenseFragment fragment;
    private Meetup meetup;
    private Map<String, Integer> splits;

    public CreateExpenseClickListener(FragmentCreateExpenseBinding binding, CreateExpenseFragment fragment, Meetup meetup, Map<String, Integer> splits) {
        this.binding = binding;
        this.fragment = fragment;
        this.meetup = meetup;
        this.splits = splits;
    }

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
