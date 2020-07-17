package com.example.meetplan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.databinding.FragmentBrowseBinding;
import com.example.meetplan.databinding.FragmentRestaurantBinding;
import com.example.meetplan.models.Event;
import com.example.meetplan.models.Restaurant;
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
    private ImmutableList<Restaurant> restaurants;
    FragmentRestaurantBinding binding;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance(String param1, String param2) {
        RestaurantFragment fragment = new RestaurantFragment();
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
        binding = FragmentRestaurantBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.etRestaurantSearch.getText().toString();
                String url = RESTAURANT_BASE_URL + "&location=" + city;
                populateRestaurants(url);
            }
        });
    }

    private void populateRestaurants(String url) {
        OkHttpClient client = new OkHttpClient();
        String value = "Bearer " + getString(R.string.yelp_api_key);
        Request request = new Request.Builder().url(url).header("Authorization", value).build();
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
                            JSONArray businesses = finalJsonObject.getJSONArray("businesses");
                            restaurants = ImmutableList.of();
                            restaurants = ImmutableList.<Restaurant>builder().addAll(Restaurant.fromJsonArray(businesses)).build();;
                            Log.i(TAG, "parsed");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}