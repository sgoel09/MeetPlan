package com.example.meetplan.expenses.create;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class SpinnerItemClick implements OnSpinerItemClick {

    private static final int SINGLE_SHARE = 1;
    private final FragmentCreateExpenseBinding binding;
    private ArrayList<String> users;
    private ImmutableList<String> usersList;
    private CreateExpenseAdapter adapter;
    private HashMap<String, Integer> splits;

    public SpinnerItemClick(FragmentCreateExpenseBinding binding, ArrayList<String> users, ImmutableList<String> usersList, CreateExpenseAdapter adapter, HashMap<String, Integer> splits) {
        this.binding = binding;
        this.users = users;
        this.usersList = usersList;
        this.adapter = adapter;
        this.splits = splits;
    }

    @Override
    public void onClick(String item, int position) {
        users.add(item);
        usersList = ImmutableList.<String>builder().addAll(usersList).add(item).build();
        splits.put(item, SINGLE_SHARE);
        adapter.updateData(users);
        String usersSoFar = binding.members.getText().toString();
        if (usersSoFar.equals("")) {
            usersSoFar = item;
        } else {
            usersSoFar += String.format("; %s", item);
        }
        binding.members.setText(usersSoFar);
    }
}
