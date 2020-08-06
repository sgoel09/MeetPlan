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
 * Allows user to search and display restaurants, filtered by city.
 * For each restaurant, user can add to meetup and view details/location on map
 * */
public class RestaurantFragment extends Fragment {

    /** Base url for accessing the business search in the Yelp API. */
    private static final String RESTAURANT_BASE_URL = "https://api.yelp.com/v3/businesses/search?";

    /** Location parameter to filter business search in the Yelp API. */
    private static final String PARAM_LOCATION = "&location=";

    /** Offset parameter to filter business search in the Yelp API. */
    private static final String PARAM_OFFSET = "&offset=";

    /** Bearer string to include before API key. */
    private static final String BEARER = "Bearer ";

    /** Authorization key to include in header when building a request. */
    private static final String AUTHORIZATION = "Authorization";

    /** Field of the JSONArray to get business. */
    private static final String JSON_FIELD_BUSINESSES = "businesses";

    /** Key for the meetup of the task in the fragment arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the city of the task in the fragment arguments. */
    private static final String KEY_CITY = "city";

    /** Limit of restaurants to display at a time. */
    private static final int LIMIT = 20;

    /** List of restaurants for the recyclerview of restaurants. */
    private ImmutableList<Restaurant> restaurants;

    /** Staggered layout manager for layout of the recyclerview of restaurants. */
    private StaggeredGridLayoutManager gridLayoutManager;

    /** Restaurant adapter for the recyclerview of restaurants. */
    private RestaurantAdapter adapter;

    /** Selected meetup for which restaurants are being browsed. */
    private Meetup meetup;

    /** Endless scroll listener for the recyclerview of restaurants. */
    private EndlessRecyclerViewScrollListener scrollListener;

    /** View binding of this fragment. */
    FragmentBrowseBinding binding;

    /** Required empty public constructor */
    public RestaurantFragment() {}

    /**
     * Creates a new instance of the fragment and saves meetup and city information in arguments.
     * @param meetup selected meetup for which restaurants are being browsed
     * @param city city for which restaurants are being browsed
     * */
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

        setsUpRecyclerView();

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(page);
            }
        };
        binding.itemRecyclerView.addOnScrollListener(scrollListener);
    }

    /** Sets of recyclerview by defining a manager, creating an adapter, and binding to the recyclerview. */
    private void setsUpRecyclerView() {
        gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_layout), StaggeredGridLayoutManager.VERTICAL);
        restaurants = ImmutableList.of();
        adapter = new RestaurantAdapter((Activity) getContext(), meetup, restaurants);
        binding.itemRecyclerView.setAdapter(adapter);
        binding.itemRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /** Loads more business search data from the Ticketmaster API.
     * @param page page of search to retrieve */
    private void loadMoreData(int page) {
        String city = binding.search.getQuery().toString();
        String url = RESTAURANT_BASE_URL + PARAM_LOCATION + city + PARAM_OFFSET + (page * LIMIT);
        populateRestaurants(url);
    }

    /** Searches restaurants from the Yelp API for a given city.
     * @param city city in which restaurants should be searched */
    private void searchByCity(String city) {
        String url = RESTAURANT_BASE_URL + PARAM_LOCATION + city;
        populateRestaurants(url);
        ((MainActivity) getActivity()).itemSelectedListener.addCity(city);
    }

    /** Creates a network call to the Yelp API and updates the adapter with the new data.
     * @param url url to access the Yelp API */
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

    /**
     * Parses events from a JSONObject and updates the restaurant adapter to bind the information to its ViewHolder.
     * @param finalJsonObject JSONObject from which information is parsed.
     * */
    private void extractData(JSONObject finalJsonObject) throws JSONException {
        JSONArray businesses = finalJsonObject.getJSONArray(JSON_FIELD_BUSINESSES);
        List<Restaurant> allRestaurants = restaurants;
        restaurants = ImmutableList.<Restaurant>builder().addAll(allRestaurants).addAll(Restaurant.fromJsonArray(businesses)).build();;
        adapter.updateData(restaurants);
        Snackbar.make(binding.getRoot(), getString(R.string.add_restaurant), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}