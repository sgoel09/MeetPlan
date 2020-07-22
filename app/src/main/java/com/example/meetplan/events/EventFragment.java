package com.example.meetplan.events;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentBrowseBinding;
import com.example.meetplan.models.Event;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.common.collect.ImmutableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private static final String EVENT_BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=";
    private static final String CITY_PARAM = "&city=";
    private static final String MEETUP_KEY = "meetup";
    private static final String CITY_KEY = "city";
    private static final String EVENTS_FIELD = "events";
    private static final String EMBEDDED_FIELD = "_embedded";
    private static final String TAG = "BrowseFragment";
    private ImmutableList<Event> events;
    private StaggeredGridLayoutManager gridLayoutManager;
    private EventAdapter adapter;
    private Meetup meetup;
    FragmentBrowseBinding binding;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(Meetup meetup, String city) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(MEETUP_KEY, meetup);
        args.putString(CITY_KEY, city);
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
        // Inflate the layout for this fragment
        binding = FragmentBrowseBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(MEETUP_KEY);
        ((MainActivity) getActivity()).itemSelectedListener.addMeetup(meetup);

        String city = getArguments().getString(CITY_KEY);
        if (city != null) {
            searchByCity(city);
            binding.search.setQuery(city, true);
        }

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchByCity(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_layout), StaggeredGridLayoutManager.VERTICAL);
        events = ImmutableList.of();
        adapter = new EventAdapter((Activity) getContext(), events, meetup);
        binding.itemRecyclerView.setAdapter(adapter);
        binding.itemRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void searchByCity(String city) {
        String url = EVENT_BASE_URL + getString(R.string.tm_api_key) + CITY_PARAM + city;
        populateEvents(url);
        ((MainActivity) getActivity()).itemSelectedListener.addCity(city);
    }

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

    private void extractData(JSONObject finalJsonObject) throws JSONException {
        JSONArray embedded = finalJsonObject.getJSONObject(EMBEDDED_FIELD).getJSONArray(EVENTS_FIELD);
        events = ImmutableList.<Event>builder().addAll(Event.fromJsonArray(embedded)).build();;
        adapter.updateData(events);
    }
}