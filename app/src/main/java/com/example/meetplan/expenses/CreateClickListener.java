package com.example.meetplan.expenses;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.expenses.create.CreateExpenseFragment;
import com.example.meetplan.models.Meetup;

public class CreateClickListener implements View.OnClickListener {

    private Context context;
    private Meetup meetup;
    private ExpenseFragment expenseFragment;

    public CreateClickListener(Context context, Meetup meetup, ExpenseFragment fragment) {
        this.context = context;
        this.meetup = meetup;
        this.expenseFragment = fragment;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        CreateExpenseFragment fragment = CreateExpenseFragment.newInstance(meetup);
        fragment.setTargetFragment(expenseFragment, 0);
        fragment.show(fm, "tag");
    }
}
