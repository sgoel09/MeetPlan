package com.example.meetplan.expenses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemTransactionBinding;
import com.example.meetplan.expenses.models.Transaction;
import com.google.common.collect.ImmutableList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Transaction> transactions;

    public TransactionAdapter(Activity context,  ImmutableList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(context.getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateData(ImmutableList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTransactionBinding binding;

        public ViewHolder(@NonNull View itemView, ItemTransactionBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(Transaction transaction) {
            binding.debtor.setText(transaction.getDebtor());
            binding.creditor.setText(transaction.getCreditor());
            binding.amount.setText(String.format("$%s", transaction.getAmount()));
        }
    }
}
