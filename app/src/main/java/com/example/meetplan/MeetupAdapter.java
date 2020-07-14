package com.example.meetplan;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemMeetupBinding;
import com.example.meetplan.fragments.DetailsFragment;
import com.google.common.collect.ImmutableList;

public class MeetupAdapter extends RecyclerView.Adapter<MeetupAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Meetup> meetups;

    public MeetupAdapter(Activity context,  ImmutableList<Meetup> meetups) {
        this.context = context;
        this.meetups = meetups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeetupBinding binding = ItemMeetupBinding.inflate(context.getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meetup meetup = meetups.get(position);
        holder.bind(meetup);
    }

    @Override
    public int getItemCount() {
        return meetups.size();
    }

    public void updateData(ImmutableList<Meetup> meetups) {
        this.meetups = meetups;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMeetupBinding binding;

        public ViewHolder(@NonNull View itemView, ItemMeetupBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("Adapter", "onclick");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Meetup meetup = meetups.get(position);
                Fragment fragment = DetailsFragment.newInstance(meetup);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        }

        public void bind(final Meetup meetup) {
            binding.tvTitle.setText(meetup.getName());
            binding.tvDescription.setText(meetup.getDescription());
        }
    }
}
