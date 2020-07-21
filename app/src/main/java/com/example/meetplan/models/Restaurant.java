package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private final String name;
    private final String id;
    private final String price;
    private final String image;
    private static final String JSON_FIELD_NAME = "name";
    private static final String JSON_FIELD_ID = "id";
    private static final String JSON_FIELD_URL = "image_url";
    private static final String JSON_FIELD_PRICE = "price";
    private static final String JSON_FIELD_LOCATION = "location";
    private final Location location;

    public Restaurant(JSONObject jsonObject) throws JSONException {
        name = checkValue(jsonObject, JSON_FIELD_NAME);
        id = checkValue(jsonObject, JSON_FIELD_ID);
        image = checkValue(jsonObject, JSON_FIELD_URL);
        price = checkValue(jsonObject, JSON_FIELD_PRICE);
        location = new Location(jsonObject.getJSONObject(JSON_FIELD_LOCATION));
    }

    public static List<Restaurant> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            restaurants.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;
    }

    private String checkValue(JSONObject jsonObject, String field) {
        try {
            String address = jsonObject.getString(field);
            return address;
        } catch (JSONException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Location getLocation() {
        return location;
    }
}
