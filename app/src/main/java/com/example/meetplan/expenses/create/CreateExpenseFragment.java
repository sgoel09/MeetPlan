package com.example.meetplan.expenses.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.User;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/** Fragment that allows users to create an expense and input information about
 * the new expense, including, name, amount, members, and how to split the expense. */
public class CreateExpenseFragment extends DialogFragment {

    /** Key for the meetup in the arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** View binding for this fragment. */
    private FragmentCreateExpenseBinding binding;

    /** Spinner dialog that displays the members of the meetup to add to the expense. */
    private SpinnerDialog spinnerDialog;

    /** Meetup for which the expense is being created. */
    private Meetup meetup;

    /** Click listener to add a member from the meeetup to the expense. */
    private AddMemberClickListener addMemberClickListener;

    /** Switch change listener for the toggle between adding all members and a select few. */
    private SwitchChangeListener switchChangeListener;

    /** Click listener for an member in the spinner dialog to be added to the expense. */
    private SpinnerItemClick spinnerItemClick;

    /** Layout Manager for a specific */
    private LinearLayoutManager layoutManager;

    /** Adapter for the recyclerview of members in an expense. */
    private CreateExpenseAdapter createExpenseAdapter;

    /** Click listener to create the expense. */
    private CreateExpenseClickListener createExpenseClickListener;

    /** Immutable list of users that are part of the expense. */
    private ImmutableList<User> usersImmutable;

    /** List of users that are part of the expense. */
    private List<User> users;

    /** Map that holds the association between each user and the number they are paying on behalf for. */
    Map<String, Integer> splits;

    /** Required empty public constructor. */
    public CreateExpenseFragment() {}

    public static CreateExpenseFragment newInstance(Meetup meetup) {
        CreateExpenseFragment fragment = new CreateExpenseFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Base_Theme_MyApp);
        binding = FragmentCreateExpenseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    /** Sets all members to the expense intially, and sets up the recylerview and click listeners. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);

        setUpAllUsers();
        setUpMembersList();

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), meetup.getMembers(), getString(R.string.add_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        spinnerItemClick = new SpinnerItemClick(users, usersImmutable, createExpenseAdapter, splits);
        spinnerDialog.bindOnSpinerListener(spinnerItemClick);

        binding.allSwitch.setChecked(true);
        binding.membersButton.setVisibility(View.GONE);
        binding.divider.setVisibility(View.GONE);
        binding.divider2.setVisibility(View.GONE);

        switchChangeListener = new SwitchChangeListener(binding, users, usersImmutable, meetup, createExpenseAdapter, splits);
        binding.allSwitch.setOnCheckedChangeListener(switchChangeListener);

        addMemberClickListener = new AddMemberClickListener(spinnerDialog);
        binding.membersButton.setOnClickListener(addMemberClickListener);

        createExpenseClickListener = new CreateExpenseClickListener(binding, this, meetup, splits);
        binding.createButton.setOnClickListener(createExpenseClickListener);
    }

    /** Sets up the recylcerview of members that are part of the expense by setting the adapter,
     * layout manager, and visibility of related views. */
    private void setUpMembersList() {
        layoutManager = new LinearLayoutManager(getContext());
        createExpenseAdapter = new CreateExpenseAdapter(getContext(), users, splits);
        binding.membersRecyclerView.setAdapter(createExpenseAdapter);
        binding.membersRecyclerView.setLayoutManager(layoutManager);
        binding.membersRecyclerView.setVisibility(View.GONE);
        binding.membersLabel.setVisibility(View.GONE);
        binding.numPeopleLabel.setVisibility(View.GONE);
    }

    /** Adds all members of the meetup to the list of users for the expense and calculates their split. */
    private void setUpAllUsers() {
        users = new ArrayList<>();
        for (String member : meetup.getMembers()) {
            users.add(new User(member));
        }
        usersImmutable = ImmutableList.<User>builder().addAll(users).build();
        calculateSplits();
    }

    /** Assigns an even split and updates the map if all members are selected for the expense. */
    private void calculateSplits() {
        splits = new HashMap<>();
        for (User user : usersImmutable) {
            splits.put(user.getUsername(), 1);
        }
    }
}