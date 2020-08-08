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

/** Fragment that displays the expenses and includes button to
 * create a new expense. When the user clicks on the calculate button,
 * the optimized transactions are calculated and displayed on the right side. */
public class ExpenseFragment extends Fragment implements PassExpense {

    /** Key for the meetup in the fragment arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the object id of the expense in the Parse databse. */
    private static final String KEY_OBJECT_ID = "objectId";

    /** Key for the SplitExpense of the expense in the Parse databse. */
    private static final String KEY_SPLIT_EXPENSE = "splitexpense";

    /** Limit of the parse querry when only looking for one item. */
    private static final int UNIQUE_LIMIT = 1;

    /** View binding for this fragment. */
    private FragmentExpenseBinding binding;

    /** Selected meetup for which expenses are displayed. */
    private Meetup meetup;

    /** Layout manager for the recyclerview of expenses. */
    private LinearLayoutManager layoutManager;

    /** Layout manager for the recyclerview of transactions. */
    private LinearLayoutManager transactionLayoutManager;

    /** Transaction adapter for the recyclerview of transactions. */
    private TransactionAdapter transactionAdapter;

    /** Expense adapter for the recyclerview of expenses. */
    private ExpenseAdapter expenseAdapter;

    /** ImmutableList of transactions for the recyclerview of transactions. */
    private ImmutableList<Transaction> allTransactions;

    /** ImmutableList of expenses for the recyclerview of expenses. */
    private ImmutableList<Expense> allExpenses;

    /** Click listener for the . */
    private CreateClickListener createClickListener;
    private ExpenseManager expenseManager;

    /** Required empty public constructor. */
    public ExpenseFragment() {}

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
        binding = FragmentExpenseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    /** Set up the adapter for expenses and transactions and sets on click listeners for the views. */
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

    /** Query each expenses when the fragment is resumed, in order to include the SplitExpense object. */
    @Override
    public void onResume() {
        super.onResume();
        Log.i("ExpenseFragment", "onResume");
        for (Expense expense : meetup.getExpenses()) {
            queryExpenses(expense);
        }
    }

    /** Sets up the recyclerview by defining a layout manager, creating an adapter, and binding to the recyclerview. */
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

    /** Queries the expense object from the Parse database, in order to include the SplitExpense
     * object of the expense that defines how the expense is split.
     * @param expense expense that is being queried */
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

    /** Updates the data list and adapter when a new expense is created and passed in from the dialog fragment.
     * @param expense new expense that is being passed */
    @Override
    public void passNewExpense(Expense expense) {
        allExpenses = ImmutableList.<Expense>builder().addAll(allExpenses).add(expense).build();
        expenseAdapter.updateData(allExpenses);
    }
}