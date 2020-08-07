package com.example.meetplan.expenses.create;

import android.view.View;
import android.widget.CompoundButton;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.User;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

/** Switch change listener for the toggle of including all members of the meetup or only a
 * select few to the new expense. Updates the information in list of users and splits. */
public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

    /** View binding for the create expense fragment. */
    private final FragmentCreateExpenseBinding binding;

    /** List of members of the meetup displayed in the spinner dialog. */
    private List<User> users;

    /** ImmutableList of members of the meetup displayed in the spinner dialog. */
    private ImmutableList<User> usersImmutable;

    /** Meetup for which the expense is being created. */
    private Meetup meetup;

    /** Adapter for the recyclerview of members in an expense. */
    private CreateExpenseAdapter adapter;

    /** Map that holds the association between each user and the number they are paying on behalf for. */
    private Map<String, Integer> splits;

    public SwitchChangeListener(FragmentCreateExpenseBinding binding, List<User> users, ImmutableList<User> usersImmutable, Meetup meetup, CreateExpenseAdapter adapter, Map<String, Integer> splits) {
        this.binding = binding;
        this.users = users;
        this.usersImmutable = usersImmutable;
        this.meetup = meetup;
        this.adapter = adapter;
        this.splits = splits;
    }

    /** Updates the visibility of the views related to the members in the create expense fragment,
     * the list of users, and portion of expense for each member. */
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

    /** Assigns an even split and updates the map if all members are selected for the expense. */
    private void calculateSplits() {
        splits.clear();
        for (User user : usersImmutable) {
            splits.put(user.getUsername(), 1);
        }
    }

    /** Updates the visibility of views relating the members in the create expense fragment. */
    private void setViews(int visibility) {
        binding.membersButton.setVisibility(visibility);
        binding.membersLabel.setVisibility(visibility);
        binding.membersRecyclerView.setVisibility(visibility);
        binding.numPeopleLabel.setVisibility(visibility);
        binding.divider.setVisibility(visibility);
        binding.divider2.setVisibility(visibility);
    }
}
