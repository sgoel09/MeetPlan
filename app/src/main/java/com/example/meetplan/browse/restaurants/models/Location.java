package com.example.meetplan.browse.restaurants.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Location {

    public String address;
    public String city;
    public String zipCode;
    public String state;
    public LatLng coordinates;
    public static final String JSON_FIELD_ADDRESS1 = "address1";
    public static final String JSON_FIELD_CITY = "city";
    public static final String JSON_FIELD_ZIP_CODE = "zip_code";
    public static final String JSON_FIELD_STATE = "state";
    public static final String JSON_FIELD_LATITUDE = "latitude";
    public static final String JSON_FIELD_LONGITUDE = "longitude";

    public Location() {}

    public Location(JSONObject jsonLocation, JSONObject jsonCoordinates) throws JSONException {
        address = checkValue(jsonLocation, JSON_FIELD_ADDRESS1);
        city = checkValue(jsonLocation, JSON_FIELD_CITY);
        zipCode = checkValue(jsonLocation, JSON_FIELD_ZIP_CODE);
        state = checkValue(jsonLocation, JSON_FIELD_STATE);
        double latitude = Double.parseDouble(checkValue(jsonCoordinates, JSON_FIELD_LATITUDE));
        double longitude = Double.parseDouble(checkValue(jsonCoordinates, JSON_FIELD_LONGITUDE));
        coordinates = new LatLng(latitude, longitude);
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    private String checkValue(JSONObject jsonObject, String field) {
        try {
            String address = jsonObject.getString(field);
            return address;
        } catch (JSONException e) {
            return null;
        }
    }
}
