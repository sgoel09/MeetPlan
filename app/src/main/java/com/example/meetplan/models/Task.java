package com.example.meetplan.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import javax.annotation.Nullable;

@ParseClassName("Task")
public class Task extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_PLACE = "place";
    public static final String KEY_ADDRESS = "address";

    public Task() {}

    public Task(String name, String place, String address) {
        put(KEY_NAME, name);
        put(KEY_PLACE, place);
        put(KEY_ADDRESS, address);
    }

    @Nullable
    public String getName() {
        return getString(KEY_NAME);
    }

    @Nullable
    public String getPlace() {
        return getString(KEY_PLACE);
    }

    @Nullable
    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

}
