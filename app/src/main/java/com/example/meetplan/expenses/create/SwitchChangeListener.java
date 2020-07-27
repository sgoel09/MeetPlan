package com.example.meetplan.expenses.create;

import android.view.View;
import android.widget.CompoundButton;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.User;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

    private final FragmentCreateExpenseBinding binding;
    private List<User> users;
    private ImmutableList<User> usersImmutable;
    private Meetup meetup;
    private CreateExpenseAdapter adapter;
    private Map<String, Integer> splits;

    public SwitchChangeListener(FragmentCreateExpenseBinding binding, List<User> users, ImmutableList<User> usersImmutable, Meetup meetup, CreateExpenseAdapter adapter, Map<String, Integer> splits) {
        this.binding = binding;
        this.users = users;
        this.usersImmutable = usersImmutable;
        this.meetup = meetup;
        this.adapter = adapter;
        this.splits = splits;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        users.clear();
        if (checked) {
            setViews(View.GONE);
            for (String member : meetup.getMembers()) {
                users.add(new User(member));
            }
            usersImmutable = ImmutableList.<User>builder().addAll(users).build();
            calculateSplits();
        } else {
            setViews(View.VISIBLE);
            usersImmutable = ImmutableList.of();
            splits.clear();
        }
        adapter.updateData(users);
    }

    private void calculateSplits() {
        splits.clear();
        for (User user : usersImmutable) {
            splits.put(user.getUsername(), 1);
        }
    }

    private void setViews(int visibility) {
        binding.membersButton.setVisibility(visibility);
        binding.membersLabel.setVisibility(visibility);
        binding.members.setVisibility(visibility);
        binding.membersRecyclerView.setVisibility(visibility);
    }
}
