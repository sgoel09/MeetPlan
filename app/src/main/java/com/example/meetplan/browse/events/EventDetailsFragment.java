package com.example.meetplan.browse.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.events.models.Venue;
import com.example.meetplan.databinding.FragmentTaskDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

/**
 * Displays the details for an event, including name, date, venue address,
 * and venue location on a Google Map.
 * */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback {

    /** View binding for this fragment. */
    private FragmentTaskDetailsBinding binding;

    /** Google map to display venue location. */
    private GoogleMap mMap;

    /** Venue object that holds all the information related to a venue. */
    private Venue venue;

    /** Required empty public constructor. */
    public EventDetailsFragment() {}

    /** Creates a new instance of the fragment and saves the venue in arguments.
     * @param venue venue for the selected event
     * */
    public static EventDetailsFragment newInstance(Venue venue) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("venue", Parcels.wrap(venue));
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
        binding = FragmentTaskDetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        venue = Parcels.unwrap(getArguments().getParcelable("venue"));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(venue.getCoordinates()).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venue.getCoordinates(), 15));
    }
}