package com.example.meetplan;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.databinding.ItemMeetupBinding;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.meetups.MeetupAdapter;
import com.example.meetplan.meetups.MeetupsFragment;
import com.example.meetplan.models.Event;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.ParseUser;

import java.util.ArrayList;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Event> events;

    public BrowseAdapter(Activity context,  ImmutableList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActivityBinding binding = ItemActivityBinding.inflate(context.getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateData(ImmutableList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemActivityBinding binding;

        public ViewHolder(@NonNull View itemView, ItemActivityBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(final Event event) {
            binding.tvName.setText(event.getName());
            binding.tvUrl.setText(event.getUrl());
        }
    }
}
