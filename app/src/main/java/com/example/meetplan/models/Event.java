package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {

    private String name;
    private String id;
    private String url;
    private String date;
    //TODO: venue and image

    public Event(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        id = jsonObject.getString("id");
        url = jsonObject.getString("url");
        date = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
    }

    public static List<Event> fromJsonArray(JSONArray eventJsonArray) throws JSONException {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventJsonArray.length(); i++) {
            events.add(new Event(eventJsonArray.getJSONObject(i)));
        }
        return events;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }
}
