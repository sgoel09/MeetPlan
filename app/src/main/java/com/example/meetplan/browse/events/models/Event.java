package com.example.meetplan.browse.events.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model data class for an event.
 * Parses and stores information for an event from a JSONObject.
 * */
@Parcel
public class Event {

    /** Field of the JSONObject to get the name of the event. */
    public static final String JSON_FIELD_NAME = "name";

    /** Field of the JSONObject to get the id of the event. */
    public static final String JSON_FIELD_ID = "id";

    /** Field of the JSONObject to get the url of the event. */
    public static final String JSON_FIELD_URL = "url";

    /** Field of the JSONObject to get the date of the event. */
    public static final String JSON_FIELD_DATES = "dates";

    /** Field of the JSONObject to get the start date of the event. */
    public static final String JSON_FIELD_START = "start";

    /** Field of the JSONObject to get the local date of the event. */
    public static final String JSON_FIELD_LOCAL_DATE = "localDate";

    /** Field of the JSONObject to get the embedded array of the event. */
    public static final String JSON_FIELD_EMBEDDED = "_embedded";

    /** Field of the JSONObject to get the venues of the event. */
    public static final String JSON_FIELD_VENUES = "venues";

    /** Field of the JSONObject to get the images of the event. */
    public static final String JSON_FIELD_IMAGES = "images";

    /** Name of the event. */
    public String name;

    /** Id of the event. */
    public String id;

    /** Url of the event. */
    public String url;

    /** Date of the event. */
    public String date;

    /** Venue of the event. */
    public Venue venue;

    /** Image of the event. */
    public String image;

    public Event() {}

    /**
     * Constructor that takes in the retrieved JSONObject and parses information.
     * @param jsonObject JSONObject from which to parse event information
     * */
    public Event(JSONObject jsonObject) throws JSONException {
        name = checkValue(jsonObject, JSON_FIELD_NAME);
        id = checkValue(jsonObject, JSON_FIELD_ID);
        url = checkValue(jsonObject, JSON_FIELD_URL);
        date = jsonObject.getJSONObject(JSON_FIELD_DATES).getJSONObject(JSON_FIELD_START).getString(JSON_FIELD_LOCAL_DATE);
        venue = new Venue(jsonObject.getJSONObject(JSON_FIELD_EMBEDDED).getJSONArray(JSON_FIELD_VENUES));
        image = jsonObject.getJSONArray(JSON_FIELD_IMAGES).getJSONObject(0).getString(JSON_FIELD_URL);
    }

    /** Creates a list of events from a JSONArray of JSONObjects with event infomration.
     * @param eventJsonArray JSONArray to parse information from
     * @return events array of events
     * */
    public static List<Event> fromJsonArray(JSONArray eventJsonArray) throws JSONException {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventJsonArray.length(); i++) {
            events.add(new Event(eventJsonArray.getJSONObject(i)));
        }
        return events;
    }

    /** Checks if the value of field exists in the JSONObject and returns the String if exists.
     * @param jsonObject JSONObject from which to parse information
     * @param field field to parse information
     * @return value String value of the field if parsed successfully
     * */
    private String checkValue(JSONObject jsonObject, String field) {
        try {
            String value = jsonObject.getString(field);
            return value;
        } catch (JSONException e) {
            return null;
        }
    }

    /** @return name of the event */
    public String getName() {
        return name;
    }

    /** @return url of the event */
    public String getUrl() {
        return url;
    }

    /** @return date of the event */
    public String getDate() {
        return date;
    }

    /** @return image of the event */
    public String getImage() {
        return image;
    }

    /** @return venue of the event */
    public Venue getVenue() {
        return venue;
    }
}
