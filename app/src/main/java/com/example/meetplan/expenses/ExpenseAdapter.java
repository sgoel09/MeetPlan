package com.example.meetplan.expenses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemExpenseBinding;
import com.example.meetplan.models.Expense;
import com.google.common.collect.ImmutableList;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemExpenseBinding binding;

        public ViewHolder(@NonNull View itemView, ItemExpenseBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(Expense expense) {
        }
    }
}
