package com.example.meetplan.browse.restaurants.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Location {

    /** Street adderess of the location. */
    public String address;

    /** City of the location. */
    public String city;

    /** Zip code of the location. */
    public String zipCode;

    /** State of the location. */
    public String state;

    /** Latitude and longitude coordinates of the location. */
    public LatLng coordinates;

    /** Field of the JSONObject to get the address of the location. */
    public static final String JSON_FIELD_ADDRESS1 = "address1";

    /** Field of the JSONObject to get the city of the location. */
    public static final String JSON_FIELD_CITY = "city";

    /** Field of the JSONObject to get the zip code of the location. */
    public static final String JSON_FIELD_ZIP_CODE = "zip_code";

    /** Field of the JSONObject to get the state of the location. */
    public static final String JSON_FIELD_STATE = "state";

    /** Field of the JSONObject to get the latitude coordinate of the location. */
    public static final String JSON_FIELD_LATITUDE = "latitude";

    /** Field of the JSONObject to get the longitude coordinate of the location. */
    public static final String JSON_FIELD_LONGITUDE = "longitude";

    /** Required empty constructor. */
    public Location() {}

    /**
     *  Constructor that takes in the retrieved JSONArray and parses information
     *  @param jsonLocation JSONObject from which to parse location information
     * */
    public Location(JSONObject jsonLocation, JSONObject jsonCoordinates) {
        address = checkValue(jsonLocation, JSON_FIELD_ADDRESS1);
        city = checkValue(jsonLocation, JSON_FIELD_CITY);
        zipCode = checkValue(jsonLocation, JSON_FIELD_ZIP_CODE);
        state = checkValue(jsonLocation, JSON_FIELD_STATE);
        double latitude = Double.parseDouble(checkValue(jsonCoordinates, JSON_FIELD_LATITUDE));
        double longitude = Double.parseDouble(checkValue(jsonCoordinates, JSON_FIELD_LONGITUDE));
        coordinates = new LatLng(latitude, longitude);
    }

    /** @return complete address of the location */
    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
    }

    /** @return latitude and longitude coordinate of the location */
    public LatLng getCoordinates() {
        return coordinates;
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
}
