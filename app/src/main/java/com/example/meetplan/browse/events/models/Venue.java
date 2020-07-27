package com.example.meetplan.browse.events.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Venue {

    private final String name;
    private final String address;
    private final String city;
    private final String state;
    private final String postalCode;
    private static final String JSON_FIELD_NAME = "name";
    private static final String JSON_FIELD_ADDRESS = "address";
    private static final String JSON_FIELD_CITY = "city";
    private static final String JSON_FIELD_STATE = "state";
    private static final String JSON_FIELD_POSTAL_CODE = "postalCode";
    private static final String JSON_FIELD_LINE1 = "line1";
    private static final String JSON_FIELD_STATE_CODE = "stateCode";



    public Venue(JSONArray jsonArray) {
        name = checkValueString(jsonArray, JSON_FIELD_NAME);
        address = checkValueObject(jsonArray, JSON_FIELD_ADDRESS, JSON_FIELD_LINE1);
        city = checkValueObject(jsonArray, JSON_FIELD_CITY, JSON_FIELD_NAME);
        state = checkValueObject(jsonArray, JSON_FIELD_STATE, JSON_FIELD_STATE_CODE);
        postalCode = checkValueString(jsonArray, JSON_FIELD_POSTAL_CODE);
    }

    private String checkValueString(JSONArray jsonArray, String field) {
        try {
            String string = jsonArray.getJSONObject(0).getString(field);
            return string;
        } catch (JSONException e) {
            return null;
        }
    }

    private String checkValueObject(JSONArray jsonArray, String object, String field) {
        try {
            String string = jsonArray.getJSONObject(0).getJSONObject(object).getString(field);
            return string;
        } catch (JSONException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, postalCode);
    }
}
