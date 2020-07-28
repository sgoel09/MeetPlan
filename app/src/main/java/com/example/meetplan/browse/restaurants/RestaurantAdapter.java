package com.example.meetplan.browse.restaurants;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meetplan.MainActivity;
import com.example.meetplan.utilities.OnDoubleTapListener;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemRestaurantBinding;
import com.example.meetplan.browse.addtask.AddTaskFragment;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.browse.restaurants.models.Restaurant;
import com.google.common.collect.ImmutableList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Restaurant> restaurants;
    private Meetup meetup;

    public RestaurantAdapter(Activity context, Meetup meetup, ImmutableList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
        this.meetup = meetup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(context.getLayoutInflater(), parent, false);
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

    public void updateData(ImmutableList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemRestaurantBinding binding;
        private Restaurant restaurant;
        private final String RESTAURANT_TAG = "Restaurant";

        public ViewHolder(View itemView, ItemRestaurantBinding bind) {
            super(bind.getRoot());
            binding = bind;
            itemView.setOnTouchListener(new OnDoubleTapListener(context) {
                @Override
                public void onDoubleTap(MotionEvent e) {
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    AddTaskFragment editNameDialogFragment = AddTaskFragment.newInstance(meetup, RESTAURANT_TAG,
                            restaurant.getName(), restaurant.getLocation().getFullAddress());
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
            });
        }

        public void bind(Restaurant restaurant) {
            this.restaurant = restaurant;
            binding.name.setText(restaurant.getName());
            binding.price.setText(restaurant.getPrice());
            binding.address.setText(restaurant.getLocation().getFullAddress());
            loadImage();
        }

        private void loadImage() {
            int imageWidth = context.getResources().getInteger(R.integer.image_width);
            int imageHeight = context.getResources().getInteger(R.integer.image_height);
            int round = context.getResources().getInteger(R.integer.round_corners);
            Glide.with(context).load(restaurant.getImage()).override(imageWidth, imageHeight).centerCrop().transform(new RoundedCorners(round)).into(binding.image);
        }
    }
}
