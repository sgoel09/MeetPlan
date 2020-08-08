package com.example.meetplan.expenses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.ItemTransactionBinding;
import com.example.meetplan.expenses.models.Transaction;
import com.google.common.collect.ImmutableList;

/**
 * Adapter for the recyclerview of transaction of the selected meetup.
 * Each item holds transaction information, including creditor, debtor, and amount.
 * */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    /** String format for the rounded off amount of the transaction with the dollar sign. */
    private static final String AMOUNT_FORMAT = "$%.2f";

    /** Context of the transaction fragment. */
    private Context context;

    /** ImmutableList of transactions the adapter holds. */
    private ImmutableList<Transaction> transactions;

    public TransactionAdapter(Context context,  ImmutableList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(((MainActivity) context).getLayoutInflater());
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

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param transactions new list of transactions with updated information
     * */
    public void updateData(ImmutableList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one transaction.
     * Binds all views in the ViewHolder to the corresponding transaction information.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** View binding for the transaction item. */
        private ItemTransactionBinding binding;

        public ViewHolder(@NonNull View itemView, ItemTransactionBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        /** Binds the transaction information to the views in the ViewHolder,
         * including creditor, debtor, and the amount.
         * @param transaction transaction for which its data is binded to
         * */
        public void bind(Transaction transaction) {
            binding.debtor.setText(transaction.getDebtor());
            binding.creditor.setText(transaction.getCreditor());
            binding.amount.setText(String.format(AMOUNT_FORMAT, transaction.getAmount()));
        }
    }
}
