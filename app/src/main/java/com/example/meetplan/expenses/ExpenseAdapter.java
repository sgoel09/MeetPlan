package com.example.meetplan.expenses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.ItemExpenseBinding;
import com.example.meetplan.expenses.models.Expense;
import com.google.common.collect.ImmutableList;

import java.util.Map;

/**
 * Adapter for the recyclerview of expenses of the selected meetup.
 * Each item holds expense information, including name, amount, and members.
 * */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    /** String format for the rounded off amount of the expense with the dollar sign. */
    private static final String AMOUNT_FORMAT = "$%.2f";

    /** Context of the expense fragment. */
    private Context context;

    /** ImmutableList of expenses the adapter holds. */
    private ImmutableList<Expense> expenses;

    public ExpenseAdapter(Context context,  ImmutableList<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(((MainActivity) context).getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param expenses new list of expenses with updated information
     * */
    public void updateData(ImmutableList<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one expense.
     * Binds all views in the ViewHolder to the corresponding expense information.
     * */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** View binding for the expense item. */
        private ItemExpenseBinding binding;

        public ViewHolder(@NonNull View itemView, ItemExpenseBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        /** Binds the expense information to the views in the ViewHolder,
         * including name, amount, the payer of the expense, and the members with
         * their share.
         * @param expense expense for which its data is binded to
         * */
        public void bind(Expense expense) {
            binding.name.setText(expense.getName());
            binding.amount.setText(String.format(AMOUNT_FORMAT, expense.getAmount()));
            binding.paid.setText(expense.getSplitExpense().getPaid());
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : expense.getSplitExpense().getSplit().entrySet()) {
                sb.append(String.format("%s (%s); ", entry.getKey(), entry.getValue()));
            }
            String membersString = sb.toString();
            membersString = membersString.substring(0, membersString.length() - 2);
            binding.members.setText(membersString);
        }
    }
}
