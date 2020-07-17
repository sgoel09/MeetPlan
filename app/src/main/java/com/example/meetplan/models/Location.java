package com.example.meetplan.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {

    private String address;
    private String city;
    private String zipCode;
    private String state;

    public Location(JSONObject jsonObject) throws JSONException {
        address = jsonObject.getString("address1");
        city = jsonObject.getString("city");
        zipCode = jsonObject.getString("zip_code");
        state = jsonObject.getString("state");
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
    }
}
