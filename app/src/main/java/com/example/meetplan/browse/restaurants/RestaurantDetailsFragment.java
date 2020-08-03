package com.example.meetplan.browse.restaurants;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.events.models.Venue;
import com.example.meetplan.browse.restaurants.models.Restaurant;
import com.example.meetplan.databinding.FragmentRestaurantBinding;
import com.example.meetplan.databinding.FragmentRestaurantDetailsBinding;
import com.example.meetplan.databinding.FragmentTaskDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

public class RestaurantDetailsFragment extends Fragment implements OnMapReadyCallback {

    private static final String KEY_LOCATION = "location";
    private static final String KEY_RESTAURANT = "restaurant";
    private FragmentRestaurantDetailsBinding binding;
    private GoogleMap mMap;
    private Restaurant restaurant;
    private com.example.meetplan.browse.restaurants.models.Location place;

    public RestaurantDetailsFragment() {
        // Required empty public constructor
    }

    public static RestaurantDetailsFragment newInstance(com.example.meetplan.browse.restaurants.models.Location location, Restaurant restaurant) {
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_LOCATION, Parcels.wrap(location));
        args.putParcelable(KEY_RESTAURANT, Parcels.wrap(restaurant));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        place = Parcels.unwrap(getArguments().getParcelable(KEY_LOCATION));
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        restaurant = Parcels.unwrap(getArguments().getParcelable(KEY_RESTAURANT));
        binding.title.setText(restaurant.getName());
        binding.info.setText(restaurant.getPrice());
        binding.address.setText(place.getFullAddress());
        binding.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getUrl()));
                getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(place.getCoordinates()).title(restaurant.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getCoordinates(), 15));
    }
}