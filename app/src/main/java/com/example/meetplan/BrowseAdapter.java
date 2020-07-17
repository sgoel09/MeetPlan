package com.example.meetplan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.models.Event;
import com.google.common.collect.ImmutableList;

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
        ItemActivityBinding binding = ItemActivityBinding.inflate(context.getLayoutInflater(), parent, false);
        return new ViewHolder(binding);
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

        public ViewHolder(ItemActivityBinding bind) {
            super(bind.getRoot());
            binding = bind;
        }

        public void bind(final Event event) {
            binding.tvName.setText(event.getName());
            binding.tvDate.setText(event.getDate());
            binding.tvAddress.setText(event.getVenue().getFullAddress());
            binding.tvVenue.setText(event.getVenue().getName());
            binding.tvUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                    ((MainActivity) context).startActivity(browserIntent);
                }
            });
        }
    }
}
