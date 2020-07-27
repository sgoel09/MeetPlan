package com.example.meetplan.utilities;

import android.content.Context;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.expenses.ExpenseFragment;
import com.example.meetplan.expenses.TransactionFragment;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Transaction;
import com.google.android.material.tabs.TabLayout;

public class TabSelectedListener implements TabLayout.OnTabSelectedListener {

    private Meetup meetup;
    private Context context;

    public TabSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            ExpenseFragment fragment = ExpenseFragment.newInstance(meetup);
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        } else if (tab.getPosition() == 1) {
            TransactionFragment fragment = new TransactionFragment();
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void addMeetup(Meetup meetup) {
        this.meetup = meetup;
    }
}
