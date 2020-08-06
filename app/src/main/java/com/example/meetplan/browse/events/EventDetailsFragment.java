package com.example.meetplan.browse.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.events.models.Event;
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

    private static final String KEY_VENUE = "venue";

    private static final String KEY_EVENT = "event";

    /** View binding for this fragment. */
    private FragmentTaskDetailsBinding binding;

    /** Google map to display venue location. */
    private GoogleMap mMap;

    /** Venue object that holds all the information related to a venue. */
    private Venue venue;

    /** Event object for which the details are displayed. */
    private Event event;

    /** Required empty public constructor. */
    public EventDetailsFragment() {}

    /** Creates a new instance of the fragment and saves the venue in arguments.
     * @param venue venue for the selected event
     * */
    public static EventDetailsFragment newInstance(Venue venue, Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_VENUE, Parcels.wrap(venue));
        args.putParcelable(KEY_EVENT, Parcels.wrap(event));
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
        venue = Parcels.unwrap(getArguments().getParcelable(KEY_VENUE));
        event = Parcels.unwrap(getArguments().getParcelable(KEY_EVENT));
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.title.setText(event.getName());
        binding.date.setText(event.getDate());
        binding.address.setText(venue.getFullAddress());
        binding.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(venue.getCoordinates()).title(venue.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venue.getCoordinates(), 15));
    }
}