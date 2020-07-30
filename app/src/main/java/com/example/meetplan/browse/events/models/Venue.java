package com.example.meetplan.browse.events.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

/**
 * Model data class for a venue.
 * Parses and stores information for an event from a JSONArray.
 * */
@Parcel
public class Venue {

    /** Name of the venue. */
    public String name;

    /** Address of the venue. */
    public String address;

    /** City of the venue. */
    public String city;

    /** State of the venue. */
    public String state;

    /** Postal code of the venue. */
    public String postalCode;

    /** Coordinates of the venue. */
    public LatLng coordinates;

    /** Field of the JSONObject to get the name of the venue. */
    public static final String JSON_FIELD_NAME = "name";

    /** Field of the JSONObject to get the address of the venue. */
    public static final String JSON_FIELD_ADDRESS = "address";

    /** Field of the JSONObject to get the city of the venue. */
    public static final String JSON_FIELD_CITY = "city";

    /** Field of the JSONObject to get the state of the venue. */
    public static final String JSON_FIELD_STATE = "state";

    /** Field of the JSONObject to get the postal code of the venue. */
    public static final String JSON_FIELD_POSTAL_CODE = "postalCode";

    /** Field of the JSONObject to get the first line of the address of the venue. */
    public static final String JSON_FIELD_LINE1 = "line1";

    /** Field of the JSONObject to get the state code of the venue. */
    public static final String JSON_FIELD_STATE_CODE = "stateCode";

    /** Field of the JSONArray to get the location of the venue. */
    public String JSON_FIELD_LOCATION = "location";

    /** Field of the JSONObject to get the latitude coordinate of the venue. */
    public String JSON_FIELD_LATITUDE = "latitude";

    /** Field of the JSONObject to get the longitude coordinate of the venue. */
    public String JSON_FIELD_LONGITUDE = "longitude";

    public Venue() {}

    /**
     * Constructor that takes in the retrieved JSONArray and parses information.
     * @param jsonArray JSONArray from which to parse venue information
     * */
    public Venue(JSONArray jsonArray) {
        name = checkValueString(jsonArray, JSON_FIELD_NAME);
        address = checkValueObject(jsonArray, JSON_FIELD_ADDRESS, JSON_FIELD_LINE1);
        city = checkValueObject(jsonArray, JSON_FIELD_CITY, JSON_FIELD_NAME);
        state = checkValueObject(jsonArray, JSON_FIELD_STATE, JSON_FIELD_STATE_CODE);
        postalCode = checkValueString(jsonArray, JSON_FIELD_POSTAL_CODE);
        double latitude = Double.parseDouble(checkValueObject(jsonArray, JSON_FIELD_LOCATION, JSON_FIELD_LATITUDE));
        double longitude = Double.parseDouble(checkValueObject(jsonArray, JSON_FIELD_LOCATION, JSON_FIELD_LONGITUDE));
        coordinates = new LatLng(latitude, longitude);
    }

    /** Checks if the value of field exists in the JSONArray and returns the String if exists.
     * @param jsonArray JSONArray from which to parse information
     * @param field field to parse information
     * @return value String value of the field if parsed successfully
     * */
    private String checkValueString(JSONArray jsonArray, String field) {
        try {
            String string = jsonArray.getJSONObject(0).getString(field);
            return string;
        } catch (JSONException e) {
            return null;
        }
    }

    /** Checks if the value of field exists in the JSONObject of the JSONArray and returns the String if exists.
     * @param jsonArray JSONArray from which to parse information
     * @param object field of the JSONObject from which to parse information
     * @param field field to parse information
     * @return value String value of the field if parsed successfully
     * */
    private String checkValueObject(JSONArray jsonArray, String object, String field) {
        try {
            String string = jsonArray.getJSONObject(0).getJSONObject(object).getString(field);
            return string;
        } catch (JSONException e) {
            return null;
        }
    }

    /** @return name of the venue */
    public String getName() {
        return name;
    }

    /** @return address of the venue */
    public String getAddress() {
        return address;
    }

    /** @return complete address of the venue */
    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, postalCode);
    }

    /** @return latitude and longitude coordinate of the venue */
    public LatLng getCoordinates() {
        return coordinates;
    }
}
