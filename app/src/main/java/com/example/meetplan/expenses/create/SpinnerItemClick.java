package com.example.meetplan.expenses.create;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.User;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class SpinnerItemClick implements OnSpinerItemClick {

    private static final int SINGLE_SHARE = 1;
    private List<User> users;
    private ImmutableList<User> usersImmutable;
    private CreateExpenseAdapter adapter;
    private Map<String, Integer> splits;

    public SpinnerItemClick(List<User> users, ImmutableList<User> usersImmutable, CreateExpenseAdapter adapter, Map<String, Integer> splits) {
        this.users = users;
        this.usersImmutable = usersImmutable;
        this.adapter = adapter;
        this.splits = splits;
    }

    @Override
    public void onClick(String item, int position) {
        User newUser = new User(item);
        users.add(newUser);
        usersImmutable = ImmutableList.<User>builder().addAll(usersImmutable).add(newUser).build();
        splits.put(item, SINGLE_SHARE);
        adapter.updateData(users);
    }
}
