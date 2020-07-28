package com.example.meetplan.expenses;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentExpenseBinding;
import com.example.meetplan.expenses.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.expenses.models.Transaction;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ExpenseFragment extends Fragment implements QueryResponder {

    private static final String KEY_MEETUP = "meetup";
    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_SPLIT_EXPENSE = "splitexpense";
    private static final int UNIQUE_LIMIT = 1;
    private FragmentExpenseBinding binding;
    private Meetup meetup;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager transactionLayoutManager;
    private TransactionAdapter transactionAdapter;
    private ExpenseAdapter expenseAdapter;
    private ImmutableList<Transaction> allTransactions;
    private ImmutableList<Expense> allExpenses;
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

        createClickListener = new CreateClickListener(getContext(), meetup, this);
        binding.createButton.setOnClickListener(createClickListener);

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseManager = new ExpenseManager(transactionAdapter, allExpenses);
                expenseManager.calculateNetTotals();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ExpenseFragment", "onResume");
        for (Expense expense : meetup.getExpenses()) {
            queryExpenses(expense);
        }
    }

    private void setUpAdapters() {
        layoutManager = new LinearLayoutManager(getContext());
        allExpenses = ImmutableList.of();
        expenseAdapter = new ExpenseAdapter((Activity) getContext(), allExpenses);
        binding.expensesRecyclerView.setAdapter(expenseAdapter);
        binding.expensesRecyclerView.setLayoutManager(layoutManager);

        transactionLayoutManager = new LinearLayoutManager(getContext());
        allTransactions = ImmutableList.of();
        transactionAdapter = new TransactionAdapter(getContext(), allTransactions);
        binding.transactionsRecyclerView.setAdapter(transactionAdapter);
        binding.transactionsRecyclerView.setLayoutManager(transactionLayoutManager);
    }

    private Expense queryExpenses(final Expense expense) {
        final Expense[] newExpense = {new Expense()};
        ParseQuery<Expense> query = ParseQuery.getQuery(Expense.class);
        query.whereEqualTo(KEY_OBJECT_ID, expense.getObjectId());
        query.setLimit(UNIQUE_LIMIT);
        query.include(KEY_SPLIT_EXPENSE);
        query.findInBackground(new FindCallback<Expense>() {
            @Override
            public void done(List<Expense> expenses, ParseException e) {
                if (expenses != null) {
                    newExpense[0] = expenses.get(0);
                    allExpenses = ImmutableList.<Expense>builder().addAll(allExpenses).add(newExpense[0]).build();
                    expenseAdapter.updateData(allExpenses);
                } else {
                    Snackbar.make(binding.getRoot(), getString(R.string.expense_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
        return newExpense[0];
    }

    @Override
    public void passNewExpense(Expense expense) {
        allExpenses = ImmutableList.<Expense>builder().addAll(allExpenses).add(expense).build();
        expenseAdapter.updateData(allExpenses);
    }
}