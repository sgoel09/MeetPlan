package com.example.meetplan.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.models.Event;
import com.google.common.collect.ImmutableList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Event> events;

    public EventAdapter(Activity context, ImmutableList<Event> events) {
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
            binding.name.setText(event.getName());
            binding.date.setText(event.getDate());
            binding.address.setText(event.getVenue().getFullAddress());
            binding.venue.setText(event.getVenue().getName());
            binding.url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                    ((MainActivity) context).startActivity(browserIntent);
                }
            });
        }
    }
}
