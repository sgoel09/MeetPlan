package com.example.meetplan.expenses.create;

import android.view.View;
import android.widget.CompoundButton;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;

import java.util.ArrayList;

public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

    private final FragmentCreateExpenseBinding binding;
    private ArrayList<String> users;
    private Meetup meetup;

    public SwitchChangeListener(FragmentCreateExpenseBinding binding, ArrayList<String> users, Meetup meetup) {
        this.binding = binding;
        this.users = users;
        this.meetup = meetup;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            binding.membersButton.setVisibility(View.GONE);
            binding.membersLabel.setVisibility(View.GONE);
            binding.members.setVisibility(View.GONE);
            users.clear();
            users.addAll(meetup.getMembers());
        } else {
            binding.membersButton.setVisibility(View.VISIBLE);
            binding.membersLabel.setVisibility(View.VISIBLE);
            binding.members.setVisibility(View.VISIBLE);
            users.clear();
        }
    }
}
