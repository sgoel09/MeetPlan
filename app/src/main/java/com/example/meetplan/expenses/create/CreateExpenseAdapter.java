package com.example.meetplan.expenses.create;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemExpenseMemberBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExpenseAdapter extends RecyclerView.Adapter<CreateExpenseAdapter.ViewHolder> {

    private Activity context;
    private List<User> users;
    private Meetup meetup;
    private Map<String, Integer> splits;

    public CreateExpenseAdapter(Activity context, Meetup meetup, List<User> users, Map<String, Integer> splits) {
        this.context = context;
        this.users = users;
        this.meetup = meetup;
        this.splits = splits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseMemberBinding binding = ItemExpenseMemberBinding.inflate(context.getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateData(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemExpenseMemberBinding binding;

        public ViewHolder(@NonNull View itemView, ItemExpenseMemberBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(final User user) {
            binding.member.setText(user.getUsername());
            binding.numMembers.setText(splits.get(user.getUsername()).toString());
            binding.numMembers.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        try {
                            int num = Integer.parseInt(binding.numMembers.getText().toString());
                            splits.put(binding.member.getText().toString(), num);
                        } catch(Exception e) {
                            Snackbar.make(binding.getRoot(), context.getString(R.string.integer_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }
}
