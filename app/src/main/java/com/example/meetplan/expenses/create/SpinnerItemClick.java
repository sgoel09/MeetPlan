package com.example.meetplan.expenses.create;

import com.example.meetplan.models.User;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

/** Click listener for an item in the spinner dialog. When a member is clicked,
 * the users list and splits are updated, and the new list is sent to the adapter. */
public class SpinnerItemClick implements OnSpinerItemClick {

    /** Portion if the members is only paying for themselves. */
    private static final int SINGLE_SHARE = 1;

    /** List of members of the meetup displayed in the spinner dialog. */
    private List<User> users;

    /** ImmutableList of members of the meetup displayed in the spinner dialog. */
    private ImmutableList<User> usersImmutable;

    /** Adapter for the recyclerview of members in an expense. */
    private CreateExpenseAdapter adapter;

    /** Map that holds the association between each user and the number they are paying on behalf for. */
    private Map<String, Integer> splits;

    public SpinnerItemClick(List<User> users, ImmutableList<User> usersImmutable, CreateExpenseAdapter adapter, Map<String, Integer> splits) {
        this.users = users;
        this.usersImmutable = usersImmutable;
        this.adapter = adapter;
        this.splits = splits;
    }

    /** The user item is added to the user list, and the new list is sent to the adapter. */
    @Override
    public void onClick(String item, int position) {
        User newUser = new User(item);
        users.add(newUser);
        usersImmutable = ImmutableList.<User>builder().addAll(usersImmutable).add(newUser).build();
        splits.put(item, SINGLE_SHARE);
        adapter.updateData(users);
    }
}
