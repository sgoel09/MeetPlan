package com.example.meetplan.restaurants;

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
import com.example.meetplan.databinding.FragmentRestaurantBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Restaurant;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
 * Use the {@link RestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantFragment extends Fragment {

    private static final String TAG = "RestaurantFragment";
    private static final String RESTAURANT_BASE_URL = "https://api.yelp.com/v3/businesses/search?";
    private static final String LOCATION_PARAM = "&location=";
    private static final String BEARER = "Bearer ";
    private ImmutableList<Restaurant> restaurants;
    private GridLayoutManager gridLayoutManager;
    private RestaurantAdapter adapter;
    private Meetup meetup;
    FragmentRestaurantBinding binding;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance(Meetup meetup) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", meetup);
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
        binding = FragmentRestaurantBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        ((MainActivity) getActivity()).itemSelectedListener.addMeetup(meetup);
        binding.restaurantSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String url = RESTAURANT_BASE_URL + LOCATION_PARAM + s;
                populateRestaurants(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        gridLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_layout));
        restaurants = ImmutableList.of();
        adapter = new RestaurantAdapter((Activity) getContext(), meetup, restaurants);
        binding.restaurantsRecyclerView.setAdapter(adapter);
        binding.restaurantsRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void populateRestaurants(String url) {
        OkHttpClient client = new OkHttpClient();
        String value = BEARER + getString(R.string.yelp_api_key);
        Request request = new Request.Builder().url(url).header("Authorization", value).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Snackbar.make(binding.getRoot(), "Could not retrieve restaurant data", BaseTransientBottomBar.LENGTH_SHORT).show();
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
                    return;
                }
                final JSONObject finalJsonObject = jsonObject;
                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray businesses = finalJsonObject.getJSONArray("businesses");
                            restaurants = ImmutableList.<Restaurant>builder().addAll(Restaurant.fromJsonArray(businesses)).build();;
                            Log.i(TAG, "parsed");
                            adapter.updateData(restaurants);
                        } catch (JSONException e) {
                            Snackbar.make(binding.getRoot(), "Could not retrieve restaurant data", BaseTransientBottomBar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }
}