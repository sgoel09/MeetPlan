package com.example.meetplan.expenses;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meetplan.databinding.FragmentExpenseBinding;
import com.example.meetplan.models.Expense;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;

public class ExpenseFragment extends Fragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentExpenseBinding binding;
    private Meetup meetup;
    private LinearLayoutManager layoutManager;
    private ExpenseAdapter adapter;
    private ImmutableList<Expense> allExpenses;
    private CreateClickListener createClickListener;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    public static ExpenseFragment newInstance(Meetup meetup) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        binding = FragmentExpenseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);

        setUpAdapter();
        queryExpenses();

        createClickListener = new CreateClickListener(getContext(), meetup);
        binding.createButton.setOnClickListener(createClickListener);
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        allExpenses = ImmutableList.of();
        adapter = new ExpenseAdapter((Activity) getContext(), allExpenses);
        binding.expensesRecyclerView.setAdapter(adapter);
        binding.expensesRecyclerView.setLayoutManager(layoutManager);
    }

    private void queryExpenses() {
    }
}