package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import com.example.meetplan.expenses.ExpenseFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.models.Meetup;

public class ExpenseClickListener implements View.OnClickListener {

    private Context context;
    private Meetup meetup;

    public ExpenseClickListener(Context context, Meetup meetup) {
        this.context = context;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        ExpenseFragment fragment = ExpenseFragment.newInstance(meetup);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}
