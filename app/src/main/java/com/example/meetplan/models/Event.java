package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private static final String TAG = "Event";
    private static final String BASE_IMAGE_URL = "https://app.ticketmaster.com/discovery/v2/events/";
    private String name;
    private String id;
    private String url;
    private String date;
    private Venue venue;
    private List<String> images;
    //TODO: venue and image

    public Event(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        id = jsonObject.getString("id");
        url = jsonObject.getString("url");
        date = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
        venue = new Venue(jsonObject.getJSONObject("_embedded").getJSONArray("venues"));
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

    public Venue getVenue() {
        return venue;
    }
}
