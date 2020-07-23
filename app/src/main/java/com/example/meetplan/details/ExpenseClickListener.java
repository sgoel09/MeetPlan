package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import com.example.meetplan.ExpenseFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;

public class ExpenseClickListener implements View.OnClickListener {

    private Context context;

    public ExpenseClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        ExpenseFragment fragment = new ExpenseFragment();
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}
