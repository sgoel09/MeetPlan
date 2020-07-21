package com.example.meetplan.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Task")
public class Task extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_PLACE = "place";
    public static final String KEY_ADDRESS = "address";

    public Task() {}

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getPlace() {
        return getString(KEY_PLACE);
    }

    public void setPlace(String place) {
        put(KEY_PLACE, place);
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }
}
