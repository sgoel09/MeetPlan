package com.example.meetplan.expenses.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.expenses.QueryResponder;
import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.SplitExpense;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CreateExpenseFragment extends DialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentCreateExpenseBinding binding;
    private SpinnerDialog spinnerDialog;
    private Meetup meetup;
    private AddMemberClickListener addMemberClickListener;
    private SwitchChangeListener switchChangeListener;
    private SpinnerItemClick spinnerItemClick;
    private ArrayList<String> users = new ArrayList<>();
    HashMap<String, Integer> splits;

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

        users = new ArrayList<>();
        users.addAll(meetup.getMembers());

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), meetup.getMembers(), getString(R.string.add_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        spinnerItemClick = new SpinnerItemClick(binding, users);
        spinnerDialog.bindOnSpinerListener(spinnerItemClick);

        binding.allSwitch.setChecked(true);
        binding.membersButton.setVisibility(View.GONE);

        switchChangeListener = new SwitchChangeListener(binding, users, meetup);
        binding.allSwitch.setOnCheckedChangeListener(switchChangeListener);

        addMemberClickListener = new AddMemberClickListener(spinnerDialog);
        binding.membersButton.setOnClickListener(addMemberClickListener);

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String amount = binding.amount.getText().toString();
                SplitExpense splitExpense = new SplitExpense(calculateSplits(), ParseUser.getCurrentUser().getUsername());
                splitExpense.saveInBackground();
                Expense expense = new Expense(name, amount, splitExpense);
                expense.saveInBackground();
                ArrayList<Expense> expenses = meetup.getExpenses();
                expenses.add(expense);
                meetup.setExpenses(expenses);
                meetup.saveInBackground();
                QueryResponder mHost = (QueryResponder) getTargetFragment();
                mHost.passNewExpense(expense);
                dismiss();
            }
        });
    }

    private  HashMap<String, Integer>  calculateSplits() {
        splits = new HashMap<>();
        for (String user : users) {
            splits.put(user, 1);
        }
        return splits;
    }
}