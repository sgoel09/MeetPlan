package com.example.meetplan.expenses.create;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemExpenseMemberBinding;
import com.example.meetplan.models.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Map;

/**
 * Adapter for the recyclerview of memebers when creating an expense.
 * Each item holds the member name and the number of people they are paying on behalf for.
 * */
public class CreateExpenseAdapter extends RecyclerView.Adapter<CreateExpenseAdapter.ViewHolder> {

    /** Context of the recyclerview's fragment. */
    private Context context;

    /** List of users the adapter holds. */
    private List<User> users;

    /** Map that holds the association between each user and the number they are paying on behalf for. */
    private Map<String, Integer> splits;


    public CreateExpenseAdapter(Context context, List<User> users, Map<String, Integer> splits) {
        this.context = context;
        this.users = users;
        this.splits = splits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseMemberBinding binding = ItemExpenseMemberBinding.inflate(((MainActivity) context).getLayoutInflater());
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

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param users new list of events with updated information
     * */
    public void updateData(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one member.
     * Binds all views in the ViewHolder to the corresponding member information.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** View binding for the member item. */
        final ItemExpenseMemberBinding binding;

        public ViewHolder(@NonNull View itemView, ItemExpenseMemberBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        /** Binds the user information to the views in the ViewHolder.
         * Sets a text changed listener of the url field to save the split number for that user.
         * @param user user for which its data is binded to
         * */
        public void bind(final User user) {
            binding.member.setText(user.getUsername());
            binding.numMembers.setHint(splits.get(user.getUsername()).toString());
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
