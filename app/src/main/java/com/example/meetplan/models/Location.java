package com.example.meetplan.models;

import android.content.Context;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetplan.MainActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {

    private final String address;
    private final String city;
    private final String zipCode;
    private final String state;
    private static final String JSON_FIELD_ADDRESS1 = "address1";
    private static final String JSON_FIELD_CITY = "city";
    private static final String JSON_FIELD_ZIP_CODE = "zip_code";
    private static final String JSON_FIELD_STATE = "state";

    public Location(JSONObject jsonObject) throws JSONException {
        address = checkValue(jsonObject, JSON_FIELD_ADDRESS1);
        city = checkValue(jsonObject, JSON_FIELD_CITY);
        zipCode = checkValue(jsonObject, JSON_FIELD_ZIP_CODE);
        state = checkValue(jsonObject, JSON_FIELD_STATE);
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
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
