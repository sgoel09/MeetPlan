package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.meetplan.expenses.ExpenseFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.models.Meetup;

public class ExpenseClickListener implements View.OnClickListener {

    private Context context;
    private Meetup meetup;
    private FragmentManager fragmentManager;

    public ExpenseClickListener(FragmentManager manager, Context context, Meetup meetup) {
        this.context = context;
        this.meetup = meetup;
        this.fragmentManager = manager;
    }

    @Override
    public void onClick(View view) {
        ExpenseFragment fragment = ExpenseFragment.newInstance(meetup);
        fragmentManager.beginTransaction().addToBackStack(ExpenseFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();
    }
}
