package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Venue {

    private String name;
    private String address;
    private String city;
    private String state;
    private String postalCode;

    public Venue(JSONArray jsonArray) throws JSONException {
        name = jsonArray.getJSONObject(0).getString("name");
        address = jsonArray.getJSONObject(0).getJSONObject("address").getString("line1");
        city = jsonArray.getJSONObject(0).getJSONObject("city").getString("name");
        state = jsonArray.getJSONObject(0).getJSONObject("state").getString("stateCode");
        postalCode = jsonArray.getJSONObject(0).getString("postalCode");
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
