package com.example.meetplan.browse.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meetplan.addtask.AddTaskFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.utilities.OnDoubleTapListener;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.browse.events.models.Event;
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
        private Event event;

        public ViewHolder(@NonNull View itemView, ItemActivityBinding bind) {
            super(bind.getRoot());
            binding = bind;
            itemView.setOnTouchListener(new OnDoubleTapListener(context) {
                @Override
                public void onDoubleTap(MotionEvent e) {
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    AddTaskFragment editNameDialogFragment = AddTaskFragment.newInstance(meetup, event.getName(),
                            event.getVenue().getName(), event.getVenue().getFullAddress());
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
            });
        }

        public void bind(final Event event) {
            this.event = event;
            binding.name.setText(event.getName());
            binding.date.setText(event.getDate());
            binding.address.setText(event.getVenue().getFullAddress());
            binding.venue.setText(event.getVenue().getName());
            loadImage();
            binding.url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                    context.startActivity(browserIntent);
                }
            });
        }

        private void loadImage() {
            int imageWidth = context.getResources().getInteger(R.integer.image_width);
            int imageHeight = context.getResources().getInteger(R.integer.image_height);
            int round = context.getResources().getInteger(R.integer.round_corners);
            Glide.with(context).load(event.getImage()).override(imageWidth, imageHeight).centerCrop().transform(new RoundedCorners(round)).into(binding.image);
        }
    }
}
