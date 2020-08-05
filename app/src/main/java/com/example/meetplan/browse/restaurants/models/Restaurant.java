package com.example.meetplan.browse.restaurants.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Restaurant {

    /** Name of the restaurant. */
    public String name;

    /** Yelp ID of the restaurant. */
    public String id;

    /** Price range of the restaurant. */
    public String price;

    /** Image for the restaurant. */
    public String image;

    /** Yelp url of the restaurant. */
    public String url;

    /** Field of the JSONObject to get the name of the restaurant. */
    public final String JSON_FIELD_NAME = "name";

    /** Field of the JSONObject to get the id of the restaurant. */
    public final String JSON_FIELD_ID = "id";

    /** Field of the JSONObject to get the url of the restaurant. */
    public final String JSON_FIELD_URL = "url";

    /** Field of the JSONObject to get the image of the restaurant. */
    public final String JSON_FIELD_IMAGE_URL = "image_url";

    /** Field of the JSONObject to get the price range of the restaurant. */
    public final String JSON_FIELD_PRICE = "price";

    /** Field of the JSONObject to get the location of the restaurant. */
    public final String JSON_FIELD_LOCATION = "location";

    /** Field of the JSONObject to get the coordinates of the restaurant. */
    public final String JSON_FIELD_COORDINATES = "coordinates";

    /** Field of the JSONObject to get the location of the restaurant. */
    public Location location;

    /** Required emtpy constructor. */
    public Restaurant() {}

    /**
     *  Constructor that takes in the retrieved JSONArray and parses information
     *  @param jsonObject JSONObject from which to parse location information
     * */
    public Restaurant(JSONObject jsonObject) {
        name = checkValue(jsonObject, JSON_FIELD_NAME);
        id = checkValue(jsonObject, JSON_FIELD_ID);
        image = checkValue(jsonObject, JSON_FIELD_IMAGE_URL);
        price = checkValue(jsonObject, JSON_FIELD_PRICE);
        url = checkValue(jsonObject, JSON_FIELD_URL);
        location = createLocation(jsonObject);
    }

    /**
     * Static method to return a list of restaurants from a JSONArray of restaurants' information.
     * @param restaurantJsonArray JSONArray from which to create Restaurants
     * @return list of created Restaurant objects
     * */
    public static List<Restaurant> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            restaurants.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;
    }

    /**
     * Creates a Location object from a JSONObject with restaurant location information.
     * @param jsonObject JSONObject from which to create a Location
     * @return the created location
     * */
    private Location createLocation(JSONObject jsonObject) {
        try {
            Location location = new Location(jsonObject.getJSONObject(JSON_FIELD_LOCATION), jsonObject.getJSONObject(JSON_FIELD_COORDINATES));
            return location;
        } catch (JSONException e) {
            return null;
        }
    }

    /** Checks if the value of field exists in the JSONObject of the JSONArray and returns the String if exists.
     * @param jsonObject JSONObject from which to parse information
     * @param field field to parse information
     * @return value of the field if parsed successfully
     * */
    private String checkValue(JSONObject jsonObject, String field) {
        try {
            String address = jsonObject.getString(field);
            return address;
        } catch (JSONException e) {
            return null;
        }
    }

    /** @return name of the restaurant */
    public String getName() {
        return name;
    }

    /** @return price range of the restaurant */
    public String getPrice() {
        return price;
    }

    /** @return url of the Yelp image for the restaurant */
    public String getImage() {
        return image;
    }

    /** @return Yelp url of the restaurant */
    public String getUrl() {
        return url;
    }

    /** @return Location object of the restaurant */
    public Location getLocation() {
        return location;
    }
}
