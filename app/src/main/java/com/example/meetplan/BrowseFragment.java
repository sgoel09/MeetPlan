package com.example.meetplan;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.meetplan.databinding.FragmentBrowseBinding;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.meetups.MeetupAdapter;
import com.example.meetplan.models.Event;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {

    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=";
    private static final String TAG = "BrowseFragment";
    private ImmutableList<Event> events;
    private LinearLayoutManager layoutManager;
    private BrowseAdapter adapter;
    FragmentBrowseBinding binding;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        binding = FragmentBrowseBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.etBrowse.getText().toString();
                String url = BASE_URL + getString(R.string.api_key) + "&city=" + city;
                populateEvents(url);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        events = ImmutableList.of();
        adapter = new BrowseAdapter((Activity) getContext(), events);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(layoutManager);

    }

    private void populateEvents(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray embedded = jsonObject.getJSONObject("_embedded").getJSONArray("events");
                    events = ImmutableList.of();
                    events = ImmutableList.<Event>builder().addAll(Event.fromJsonArray(embedded)).build();;
                    Log.i(TAG, "parsed");
                    adapter.updateData(events);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}