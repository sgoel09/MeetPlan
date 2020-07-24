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
import com.example.meetplan.models.Transaction;
import com.google.common.collect.ImmutableList;

public class ExpenseFragment extends Fragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentExpenseBinding binding;
    private Meetup meetup;
    private LinearLayoutManager layoutManager;
    private ExpenseAdapter expenseAdapter;
    private ImmutableList<Expense> allExpenses;
    private LinearLayoutManager transactionLayoutManager;
    private TransactionAdapter transactionAdapter;
    private ImmutableList<Transaction> allTransactions;
    private CreateClickListener createClickListener;
    private ExpenseManager expenseManager;

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

        setUpAdapters();
        queryExpenses();

        createClickListener = new CreateClickListener(getContext(), meetup);
        binding.createButton.setOnClickListener(createClickListener);

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseManager = new ExpenseManager(meetup, transactionAdapter);
            }
        });
    }

    private void setUpAdapters() {
        layoutManager = new LinearLayoutManager(getContext());
        allExpenses = ImmutableList.of();
        expenseAdapter = new ExpenseAdapter((Activity) getContext(), allExpenses);
        binding.expensesRecyclerView.setAdapter(expenseAdapter);
        binding.expensesRecyclerView.setLayoutManager(layoutManager);

        transactionLayoutManager = new LinearLayoutManager(getContext());
        allTransactions = ImmutableList.of();
        transactionAdapter = new TransactionAdapter((Activity) getContext(), allTransactions);
        binding.transactionsRecyclerView.setAdapter(transactionAdapter);
        binding.transactionsRecyclerView.setLayoutManager(transactionLayoutManager);
    }

    private void queryExpenses() {
    }
}