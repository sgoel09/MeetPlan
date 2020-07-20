package com.example.meetplan.restaurants;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemRestaurantBinding;
import com.example.meetplan.models.Restaurant;
import com.google.common.collect.ImmutableList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Restaurant> restaurants;

    public RestaurantAdapter(Activity context, ImmutableList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(context.getLayoutInflater(), parent, false);
        return new ViewHolder(binding);
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

        ItemRestaurantBinding binding;

        public ViewHolder(ItemRestaurantBinding bind) {
            super(bind.getRoot());
            binding = bind;
        }

        public void bind(final Restaurant restaurant) {
            binding.tvName.setText(restaurant.getName());
            binding.tvPrice.setText(restaurant.getPrice());
            binding.tvAddress.setText(restaurant.getLocation().getFullAddress());
        }
    }
}
