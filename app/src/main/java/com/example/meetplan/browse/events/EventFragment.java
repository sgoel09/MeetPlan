package com.example.meetplan.browse.events;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.meetplan.utilities.EndlessRecyclerViewScrollListener;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentBrowseBinding;
import com.example.meetplan.browse.events.models.Event;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.common.collect.ImmutableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Allows user to search and display events, filtered by city.
 * For each event, user can add to meetup and view details/location on map
 * */
public class EventFragment extends Fragment {

    /** Base url for accessing the event search in the Ticketmaster API. */
    private static final String EVENT_BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=";

    /** City parameter to filter event search in the Ticketmaster API. */
    private static final String PARAM_CITY = "&city=";

    /** Page parameter to filter event search in the Ticketmaster API. */
    private static final String PARAM_PAGE = "&page=";

    /** Key for the meetup of the task in the fragment arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the city of the task in the fragment arguments. */
    private static final String KEY_CITY = "city";

    /** Field of the JSONArray to get events. */
    private static final String JSON_FIELD_EVENTS = "events";

    /** Field of the JSONArray to get the embedded information of the event. */
    private static final String JSON_FIELD_EMBEDDED = "_embedded";

    /** List of events for the recyclerview of events. */
    private ImmutableList<Event> events;

    /** Staggered layout manager for layout of the recyclerview of events. */
    private StaggeredGridLayoutManager gridLayoutManager;

    /** Event adapter for the recyclerview of events. */
    private EventAdapter adapter;

    /** Selected meetup for which events are being browsed. */
    private Meetup meetup;

    /** Endless scroll listener for the recyclerview of events. */
    private EndlessRecyclerViewScrollListener scrollListener;

    /** View binding of this fragment. */
    FragmentBrowseBinding binding;

    /** Required empty public constructor */
    public EventFragment() {}

    /**
     * Creates a new instance of the fragment and saves meetup and city information in arguments.
     * @param meetup selected meetup for which events are being browsed
     * @param city city for which events are being browsed
     * */
    public static EventFragment newInstance(Meetup meetup, String city) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putString(KEY_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialSharedAxis forwardTransition =  new MaterialSharedAxis(MaterialSharedAxis.X, true);
        setReenterTransition(forwardTransition);
        MaterialSharedAxis backwardTransition =  new MaterialSharedAxis(MaterialSharedAxis.X, false);
        setExitTransition(backwardTransition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrowseBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(true);
        return view;
    }

    /** Get information from the arguments to set views and listeners. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);
        ((MainActivity) getActivity()).itemSelectedListener.addMeetup(meetup);

        setUpRecyclerview();

        String city = getArguments().getString(KEY_CITY);
        if (city != null) {
            searchByCity(city);
            binding.search.setQuery(city, false);
        }

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                events = ImmutableList.of();
                searchByCity(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(page);
            }
        };
        binding.itemRecyclerView.addOnScrollListener(scrollListener);
    }

    /** Sets of recyclerview by defining a manager, creating an adapter, and binding to the recyclerview. */
    private void setUpRecyclerview() {
        gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_layout), StaggeredGridLayoutManager.VERTICAL);
        events = ImmutableList.of();
        adapter = new EventAdapter((Activity) getContext(), events, meetup);
        binding.itemRecyclerView.setAdapter(adapter);
        binding.itemRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /** Loads more event search data from the Ticketmaster API.
     * @param page page of search to retrieve */
    private void loadMoreData(int page) {
        String city = binding.search.getQuery().toString();
        String url = EVENT_BASE_URL + getString(R.string.tm_api_key) + PARAM_CITY + city + PARAM_PAGE + page;
        populateEvents(url);
    }

    /** Searches events from the Ticketmaster API for a given city.
     * @param city city in which events should be searched */
    private void searchByCity(String city) {
        String url = EVENT_BASE_URL + getString(R.string.tm_api_key) + PARAM_CITY + city;
        populateEvents(url);
        ((MainActivity) getActivity()).itemSelectedListener.addCity(city);
    }

    /** Creates a network call to the Ticketmaster API and updates the adapter with the new data.
     * @param url url to access the Ticketmaster API */
    private void populateEvents(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Snackbar.make(binding.getRoot(), getString(R.string.event_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(myResponse);
                } catch (JSONException e) {
                    Snackbar.make(binding.getRoot(), getString(R.string.event_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject finalJsonObject = jsonObject;
                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            extractData(finalJsonObject);
                        } catch (JSONException e) {
                            Snackbar.make(binding.getRoot(), getString(R.string.event_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }

    /**
     * Parses events from a JSONObject and updates the event adapter to bind the information to its ViewHolder.
     * @param finalJsonObject JSONObject from which information is parsed.
     * */
    private void extractData(JSONObject finalJsonObject) throws JSONException {
        JSONArray embedded = finalJsonObject.getJSONObject(JSON_FIELD_EMBEDDED).getJSONArray(JSON_FIELD_EVENTS);
        List<Event> allEvents = events;
        events = ImmutableList.<Event>builder().addAll(allEvents).addAll(Event.fromJsonArray(embedded)).build();;
        adapter.updateData(events);
    }
}