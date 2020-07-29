package com.example.meetplan.browse.events;

import android.content.Context;
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
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.addtask.AddTaskFragment;
import com.example.meetplan.browse.events.models.Event;
import com.example.meetplan.databinding.ItemActivityBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.utilities.OnDoubleTapListener;
import com.google.common.collect.ImmutableList;

/**
 * Adapter for the recyclerview of events when browsing.
 * Each item holds fundamental event information and is displayed in a staggered layout.
 * */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    /** Context of the recyclerview's fragment. */
    private Context context;

    /** List of events the adapter holds. */
    private ImmutableList<Event> events;

    /** Selected meetup for which events are browsed. */
    private Meetup meetup;

    /** Constructor to create and set up an adapter for the events.
     * @param context context of the recyclerview's fragment
     * @param events list of events the adapter holds
     * @param meetup selected meetup for which events are browsed
     * */
    public EventAdapter(Context context, ImmutableList<Event> events, Meetup meetup) {
        this.context = context;
        this.events = events;
        this.meetup = meetup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActivityBinding binding = ItemActivityBinding.inflate(((MainActivity) context).getLayoutInflater(), parent, false);
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

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param events new list of events with updated information
     * */
    public void updateData(ImmutableList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one event.
     * Binds all views in the ViewHolder to the corresponding event data.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** View binding for the event item. */
        final ItemActivityBinding binding;

        /** Event of the specific ViewHolder. */
        private Event event;

        /** Sets a double tap gesture listener for the ViewHolder,
         * on which a new AddTaskFragment is created and displayed. */
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

        /** Binds the event information to the views in the ViewHolder.
         * Sets a click listener of the url field to open up an internet browser with the url of the event.
         * @param event event for which its data is binded to
         * */
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

        /** Loads the event image in the image view,
         * with a crop and rounded corners transform applied. */
        private void loadImage() {
            int imageWidth = context.getResources().getInteger(R.integer.image_width);
            int imageHeight = context.getResources().getInteger(R.integer.image_height);
            int round = context.getResources().getInteger(R.integer.round_corners);
            Glide.with(context).load(event.getImage()).override(imageWidth, imageHeight).centerCrop().transform(new RoundedCorners(round)).into(binding.image);
        }
    }
}
