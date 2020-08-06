package com.example.meetplan.browse.restaurants;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
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
import com.example.meetplan.browse.restaurants.models.Restaurant;
import com.example.meetplan.databinding.ItemRestaurantBinding;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;

/**
 * Adapter for the recyclerview of restaurants when browsing.
 * Each item holds fundamental restaurant information and is displayed in a staggered layout.
 * */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    /** Context of the recyclerview's fragment. */
    private Context context;

    /** List of restaurants the adapter holds. */
    private ImmutableList<Restaurant> restaurants;

    /** Selected meetup for which events are browsed. */
    private Meetup meetup;

    /** Tag name for the restaurant items. */
    private final String RESTAURANT_TAG = "Restaurant";

    /** Constructor to create and set up an adapter for the events.
     * @param context context of the recyclerview's fragment
     * @param restaurants list of restaurants the adapter holds
     * @param meetup selected meetup for which restaurants are browsed
     * */
    public RestaurantAdapter(Context context, Meetup meetup, ImmutableList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
        this.meetup = meetup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(((MainActivity) context).getLayoutInflater(), parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param restaurants new list of restaurants with updated information
     * */
    public void updateData(ImmutableList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one restaurant.
     * Binds all views in the ViewHolder to the corresponding event data.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** View binding for the restaurant item. */
        final ItemRestaurantBinding binding;

        /** Restaurant of the specific ViewHolder. */
        private Restaurant restaurant;

        /** Gesture detector for the ViewHolder. */
        private GestureDetector detector;

        /** Sets a touch listener for the ViewHolder, with calls the gesture detector
         * to determine what type of gesture occurred and proceed accordingly. */
        public ViewHolder(View itemView, ItemRestaurantBinding bind) {
            super(bind.getRoot());
            binding = bind;
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return detector.onTouchEvent(motionEvent);
                }
            });
        }

        /** Binds the restaurant information to the views in the ViewHolder.
         * @param restaurant restaurant for which its data is binded to
         * */
        public void bind(Restaurant restaurant) {
            detector = new GestureDetector(context, new GestureListener(restaurant));
            this.restaurant = restaurant;
            binding.name.setText(restaurant.getName());
            binding.price.setText(restaurant.getPrice());
            binding.address.setText(restaurant.getLocation().getFullAddress());
            loadImage();
        }

        /** Loads the restaurant image in the image view,
         * with a crop and rounded corners transform applied. */
        private void loadImage() {
            int imageWidth = context.getResources().getInteger(R.integer.image_width);
            int imageHeight = context.getResources().getInteger(R.integer.image_height);
            int round = context.getResources().getInteger(R.integer.round_corners);
            Glide.with(context).load(restaurant.getImage()).override(imageWidth, imageHeight).centerCrop().transform(new RoundedCorners(round)).into(binding.image);
        }
    }

    /**
     * Gesture listener to determine difference between single tap and double tap.
     * Moves to the restaurant details fragment on single tap, and add task fragment
     * on double tap.
     * */
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /** Restaurant for which the gesture detector is set for. */
        private Restaurant restaurant;

        /**
         * Constructor to initialize the gesture detector.
         * @param restaurant event for which gesture detector is set for
         * */
        public GestureListener(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("onDown", e.getAction() + "");
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("onSingleTapUp", e.getAction() + "");
            return super.onSingleTapUp(e);
        }

        /** When a single tap is confirmed, replace current fragment with the restaurant
         * details fragment to display specific restaurant information. */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("onSingleTapConfirmed", e.getAction() + "");
            RestaurantDetailsFragment fragment = RestaurantDetailsFragment.newInstance(restaurant.getLocation(), restaurant);
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(RestaurantFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("onDoubleTap", e.getAction() + "");
            return true;
        }

        /** On a double tap, show the add task dialog fragment to prompt users to add this
         * restaurant to their selected meetup. */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("onDoubleTapEvent", e.getAction() + "");
            if (e.getAction() == 1) {
                FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                AddTaskFragment editNameDialogFragment = AddTaskFragment.newInstance(meetup, RESTAURANT_TAG,
                        restaurant.getName(), restaurant.getLocation().getFullAddress());
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
            return super.onDoubleTapEvent(e);
        }
    }
}
