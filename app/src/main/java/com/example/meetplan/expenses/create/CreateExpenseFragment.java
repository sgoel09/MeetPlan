package com.example.meetplan.expenses.create;

import android.app.Activity;
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

public class CreateExpenseFragment extends DialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentCreateExpenseBinding binding;
    private SpinnerDialog spinnerDialog;
    private Meetup meetup;
    private AddMemberClickListener addMemberClickListener;
    private SwitchChangeListener switchChangeListener;
    private SpinnerItemClick spinnerItemClick;
    private LinearLayoutManager layoutManager;
    private CreateExpenseAdapter createExpenseAdapter;
    private CreateExpenseClickListener createExpenseClickListener;
    private ImmutableList<User> usersImmutable;
    private List<User> users;
    Map<String, Integer> splits;

    public CreateExpenseFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        binding = FragmentCreateExpenseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);

        setUpAllUsers();
        setUpMembersList();

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), meetup.getMembers(), getString(R.string.add_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        spinnerItemClick = new SpinnerItemClick(binding, users, usersImmutable, createExpenseAdapter, splits);
        spinnerDialog.bindOnSpinerListener(spinnerItemClick);

        binding.allSwitch.setChecked(true);
        binding.membersButton.setVisibility(View.GONE);

        switchChangeListener = new SwitchChangeListener(binding, users, usersImmutable, meetup, createExpenseAdapter, splits);
        binding.allSwitch.setOnCheckedChangeListener(switchChangeListener);

        addMemberClickListener = new AddMemberClickListener(spinnerDialog);
        binding.membersButton.setOnClickListener(addMemberClickListener);

        createExpenseClickListener = new CreateExpenseClickListener(binding, this, meetup, splits);
        binding.createButton.setOnClickListener(createExpenseClickListener);
    }

    private void setUpMembersList() {
        layoutManager = new LinearLayoutManager(getContext());
        createExpenseAdapter = new CreateExpenseAdapter((Activity) getContext(), meetup, users, splits);
        binding.membersRecyclerView.setAdapter(createExpenseAdapter);
        binding.membersRecyclerView.setLayoutManager(layoutManager);
        binding.membersRecyclerView.setVisibility(View.GONE);
        binding.membersLabel.setVisibility(View.GONE);
        binding.numPeopleLabel.setVisibility(View.GONE);
    }

    private void setUpAllUsers() {
        users = new ArrayList<>();
        for (String member : meetup.getMembers()) {
            users.add(new User(member));
        }
        usersImmutable = ImmutableList.<User>builder().addAll(users).build();
        calculateSplits();
    }

    private void calculateSplits() {
        splits = new HashMap<>();
        for (User user : usersImmutable) {
            splits.put(user.getUsername(), 1);
        }
    }
}