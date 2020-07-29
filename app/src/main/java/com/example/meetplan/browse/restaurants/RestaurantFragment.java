package com.example.meetplan.browse.restaurants;

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
import com.example.meetplan.models.Meetup;
import com.example.meetplan.browse.restaurants.models.Restaurant;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantFragment extends Fragment {

    private static final String TAG = "RestaurantFragment";
    private static final String RESTAURANT_BASE_URL = "https://api.yelp.com/v3/businesses/search?";
    private static final String PARAM_LOCATION = "&location=";
    private static final String PARAM_OFFSET = "&offset=";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BUSINESSES_FIELD = "businesses";
    private static final String KEY_MEETUP = "meetup";
    private static final String KEY_CITY = "city";
    private static final int LIMIT = 20;
    private ImmutableList<Restaurant> restaurants;
    private StaggeredGridLayoutManager gridLayoutManager;
    private RestaurantAdapter adapter;
    private Meetup meetup;
    private EndlessRecyclerViewScrollListener scrollListener;
    FragmentBrowseBinding binding;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance(Meetup meetup, String city) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putString(KEY_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialSharedAxis backwardTransition =  new MaterialSharedAxis(MaterialSharedAxis.X, false);
        setReenterTransition(backwardTransition);
        MaterialSharedAxis forwardTransition =  new MaterialSharedAxis(MaterialSharedAxis.X, true);
        setExitTransition(forwardTransition);
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
        meetup = getArguments().getParcelable(KEY_MEETUP);
        ((MainActivity) getActivity()).itemSelectedListener.addMeetup(meetup);

        final String city = getArguments().getString(KEY_CITY);
        if (city != null) {
            searchByCity(city);
            binding.search.setQuery(city, false);
        }

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                restaurants = ImmutableList.of();
                searchByCity(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_layout), StaggeredGridLayoutManager.VERTICAL);
        restaurants = ImmutableList.of();
        adapter = new RestaurantAdapter((Activity) getContext(), meetup, restaurants);
        binding.itemRecyclerView.setAdapter(adapter);
        binding.itemRecyclerView.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(page);
            }
        };
        binding.itemRecyclerView.addOnScrollListener(scrollListener);
    }

    private void loadMoreData(int page) {
        String city = binding.search.getQuery().toString();
        String url = RESTAURANT_BASE_URL + PARAM_LOCATION + city + PARAM_OFFSET + (page * LIMIT);
        populateRestaurants(url);
    }

    private void searchByCity(String city) {
        String url = RESTAURANT_BASE_URL + PARAM_LOCATION + city;
        populateRestaurants(url);
        ((MainActivity) getActivity()).itemSelectedListener.addCity(city);
    }

    private void populateRestaurants(String url) {
        OkHttpClient client = new OkHttpClient();
        String value = BEARER + getString(R.string.yelp_api_key);
        Request request = new Request.Builder().url(url).header(AUTHORIZATION, value).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Snackbar.make(binding.getRoot(), getString(R.string.restaurant_error), BaseTransientBottomBar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(myResponse);
                } catch (JSONException e) {
                    Snackbar.make(binding.getRoot(), getString(R.string.restaurant_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject finalJsonObject = jsonObject;
                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            extractData(finalJsonObject);
                        } catch (JSONException e) {
                            Snackbar.make(binding.getRoot(), getString(R.string.restaurant_error), BaseTransientBottomBar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }

    private void extractData(JSONObject finalJsonObject) throws JSONException {
        JSONArray businesses = finalJsonObject.getJSONArray(BUSINESSES_FIELD);
        List<Restaurant> allRestaurants = restaurants;
        restaurants = ImmutableList.<Restaurant>builder().addAll(allRestaurants).addAll(Restaurant.fromJsonArray(businesses)).build();;
        adapter.updateData(restaurants);
    }
}