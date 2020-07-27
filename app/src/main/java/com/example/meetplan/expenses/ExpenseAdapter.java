package com.example.meetplan.expenses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemExpenseBinding;
import com.example.meetplan.expenses.models.Expense;
import com.google.common.collect.ImmutableList;

import java.util.Map;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Expense> expenses;

    public ExpenseAdapter(Activity context,  ImmutableList<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(context.getLayoutInflater());
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

    public void updateData(ImmutableList<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemExpenseBinding binding;

        public ViewHolder(@NonNull View itemView, ItemExpenseBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(Expense expense) {
            binding.name.setText(expense.getName());
            binding.amount.setText(String.format("$%s", expense.getAmount()));
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
