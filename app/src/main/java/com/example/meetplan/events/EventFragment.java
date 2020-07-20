package com.example.meetplan.events;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.restaurants.RestaurantFragment;
import com.example.meetplan.databinding.FragmentBrowseBinding;
import com.example.meetplan.models.Event;
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
    private static final String TAG = "BrowseFragment";
    private ImmutableList<Event> events;
    private GridLayoutManager gridLayoutManager;
    private EventAdapter adapter;
    FragmentBrowseBinding binding;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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
        binding.svEvents.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String url = EVENT_BASE_URL + getString(R.string.tm_api_key) + "&city=" + s;
                populateEvents(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        binding.btnRestaurantNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RestaurantFragment();
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        events = ImmutableList.of();
        adapter = new EventAdapter((Activity) getContext(), events);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(gridLayoutManager);
    }

    private void populateEvents(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse");
                final String myResponse = response.body().string();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(myResponse);
                } catch (JSONException e) {
                    Log.i(TAG, "onFailure");
                }
                final JSONObject finalJsonObject = jsonObject;
                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray embedded = finalJsonObject.getJSONObject("_embedded").getJSONArray("events");
                            events = ImmutableList.of();
                            events = ImmutableList.<Event>builder().addAll(Event.fromJsonArray(embedded)).build();;
                            Log.i(TAG, "parsed");
                            adapter.updateData(events);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}