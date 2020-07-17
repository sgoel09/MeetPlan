package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private String name;
    private String id;
    private String price;
    private String image;
    private Location location;

    public Restaurant(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        id = jsonObject.getString("id");
        image = jsonObject.getString("image_url");
        price = jsonObject.getString("price");
        location = new Location(jsonObject.getJSONObject("location"));
    }

    public static List<Restaurant> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            restaurants.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;
    }
}
