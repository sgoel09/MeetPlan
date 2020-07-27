package com.example.meetplan.expenses.create;

import android.view.View;
import android.widget.CompoundButton;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;

public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

    private final FragmentCreateExpenseBinding binding;
    private ArrayList<String> users;
    private ImmutableList<String> usersList;
    private Meetup meetup;
    private CreateExpenseAdapter adapter;
    private HashMap<String, Integer> splits;

    public SwitchChangeListener(FragmentCreateExpenseBinding binding, ArrayList<String> users, ImmutableList<String> usersList, Meetup meetup, CreateExpenseAdapter adapter, HashMap<String, Integer> splits) {
        this.binding = binding;
        this.users = users;
        this.usersList = usersList;
        this.meetup = meetup;
        this.adapter = adapter;
        this.splits = splits;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        users.clear();
        if (checked) {
            setViews(View.GONE);
            users.addAll(meetup.getMembers());
            usersList = ImmutableList.<String>builder().addAll(meetup.getMembers()).build();
            calculateSplits();
        } else {
            setViews(View.VISIBLE);
            usersList = ImmutableList.of();
            splits.clear();
        }
        adapter.updateData(users);
    }

    private void calculateSplits() {
        splits.clear();
        for (String user : usersList) {
            splits.put(user, 1);
        }
    }

    private void setViews(int visibility) {
        binding.membersButton.setVisibility(visibility);
        binding.membersLabel.setVisibility(visibility);
        binding.members.setVisibility(visibility);
        binding.membersRecyclerView.setVisibility(visibility);
    }
}
