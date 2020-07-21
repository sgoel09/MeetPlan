package com.example.meetplan.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.models.Event;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Event> events;
    private Meetup meetup;

    public EventAdapter(Activity context, ImmutableList<Event> events, Meetup meetup) {
        this.context = context;
        this.events = events;
        this.meetup = meetup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActivityBinding binding = ItemActivityBinding.inflate(context.getLayoutInflater(), parent, false);
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

        final ItemActivityBinding binding;

        public ViewHolder(@NonNull View itemView, ItemActivityBinding bind) {
            super(bind.getRoot());
            binding = bind;
            itemView.setOnTouchListener(new OnDoubleTapListener(context) {
                @Override
                public void onDoubleTap(MotionEvent e) {
                    Toast.makeText(context, "Double Tap", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    AddEventFragment editNameDialogFragment = AddEventFragment.newInstance(meetup);
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
            });
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
