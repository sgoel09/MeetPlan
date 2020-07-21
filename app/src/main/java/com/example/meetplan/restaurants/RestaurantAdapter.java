package com.example.meetplan.restaurants;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.OnDoubleTapListener;
import com.example.meetplan.databinding.ItemRestaurantBinding;
import com.example.meetplan.events.AddEventFragment;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Restaurant;
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
                    Toast.makeText(context, "Double Tap", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    AddEventFragment editNameDialogFragment = AddEventFragment.newInstance(meetup, RESTAURANT_TAG,
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
        }
    }
}
