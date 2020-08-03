package com.example.meetplan.browse.restaurants.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Restaurant {

    public String name;
    public String id;
    public String price;
    public String image;
    public String url;
    public final String JSON_FIELD_NAME = "name";
    public final String JSON_FIELD_ID = "id";
    public final String JSON_FIELD_URL = "url";
    public final String JSON_FIELD_IMAGE_URL = "image_url";
    public final String JSON_FIELD_PRICE = "price";
    public final String JSON_FIELD_LOCATION = "location";
    public final String JSON_FIELD_COORDINATES = "coordinates";
    public Location location;

    public Restaurant() {}

    public Restaurant(JSONObject jsonObject) {
        name = checkValue(jsonObject, JSON_FIELD_NAME);
        id = checkValue(jsonObject, JSON_FIELD_ID);
        image = checkValue(jsonObject, JSON_FIELD_IMAGE_URL);
        price = checkValue(jsonObject, JSON_FIELD_PRICE);
        url = checkValue(jsonObject, JSON_FIELD_URL);
        location = createLocation(jsonObject);
    }

    public static List<Restaurant> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            restaurants.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;
    }

    private Location createLocation(JSONObject jsonObject) {
        try {
            Location location = new Location(jsonObject.getJSONObject(JSON_FIELD_LOCATION), jsonObject.getJSONObject(JSON_FIELD_COORDINATES));
            return location;
        } catch (JSONException e) {
            return null;
        }
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

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public Location getLocation() {
        return location;
    }
}
