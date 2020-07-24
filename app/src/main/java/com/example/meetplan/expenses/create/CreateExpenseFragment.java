package com.example.meetplan.expenses.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.profile.ProfilePicCallBack;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CreateExpenseFragment extends DialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentCreateExpenseBinding binding;
    private SpinnerDialog spinnerDialog;
    private Meetup meetup;
    private AddMemberClickListener addMemberClickListener;
    private ArrayList<String> users = new ArrayList<>();

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

        users = meetup.getMembers();

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), meetup.getMembers(), getString(R.string.add_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        binding.allSwitch.setChecked(true);
        binding.membersButton.setVisibility(View.GONE);
        binding.allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    binding.membersButton.setVisibility(View.GONE);
                    users.clear();
                    users.addAll(meetup.getMembers());
                } else {
                    binding.membersButton.setVisibility(View.VISIBLE);
                    users.clear();
                }
            }
        });

        addMemberClickListener = new AddMemberClickListener(spinnerDialog);
        binding.membersButton.setOnClickListener(addMemberClickListener);

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                double amount = Double.parseDouble(binding.amount.getText().toString());
                Expense expense = new Expense(name, amount, users);
                expense.saveInBackground();
                ArrayList<Expense> expenses = meetup.getExpenses();
                expenses.add(expense);
                meetup.setExpenses(expenses);
                meetup.saveInBackground();
                dismiss();
            }
        });

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                users.add(item);
            }
        });
    }
}