package com.example.meetplan.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private static final String TAG = "Event";
    private static final String BASE_IMAGE_URL = "https://app.ticketmaster.com/discovery/v2/events/";
    private static final String JSON_FIELD_NAME = "name";
    private static final String JSON_FIELD_ID = "id";
    private static final String JSON_FIELD_URL = "url";
    private static final String JSON_FIELD_DATES = "dates";
    private static final String JSON_FIELD_START = "start";
    private static final String JSON_FIELD_LOCAL_DATE = "localDate";
    private static final String JSON_FIELD_EMBEDDED = "_embedded";
    private static final String JSON_FIELD_VENUES = "venues";
    private final String name;
    private final String id;
    private final String url;
    private final String date;
    private final Venue venue;
    private String image;
    //TODO: venue and image

    public Event(JSONObject jsonObject) throws JSONException {
        name = checkValue(jsonObject, JSON_FIELD_NAME);
        id = checkValue(jsonObject, JSON_FIELD_ID);
        url = checkValue(jsonObject, JSON_FIELD_URL);
        date = jsonObject.getJSONObject(JSON_FIELD_DATES).getJSONObject(JSON_FIELD_START).getString(JSON_FIELD_LOCAL_DATE);
        venue = new Venue(jsonObject.getJSONObject(JSON_FIELD_EMBEDDED).getJSONArray(JSON_FIELD_VENUES));
        image = jsonObject.getJSONArray("images").getJSONObject(0).getString("url");
    }

    public static List<Event> fromJsonArray(JSONArray eventJsonArray) throws JSONException {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventJsonArray.length(); i++) {
            events.add(new Event(eventJsonArray.getJSONObject(i)));
        }
        return events;
    }

    private String checkValue(JSONObject jsonObject, String field) {
        try {
            String address = jsonObject.getString(field);
            return address;
        } catch (JSONException e) {
            return null;
        }
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

    public String getImage() {
        return image;
    }

    public Venue getVenue() {
        return venue;
    }
}
