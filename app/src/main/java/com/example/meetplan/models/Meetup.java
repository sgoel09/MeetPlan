package com.example.meetplan.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Meetup")
public class Meetup extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_INVITES = "invites";
    public static final String KEY_DATE = "date";
    public static final String KEY_TASK = "task";

    public Meetup() {}

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public Date getDate() { return getDate(KEY_DATE); }

    public void setDate(Date date) { put(KEY_DATE, date); }

    public Task getTask() { return (Task) getParseObject(KEY_TASK); }

    public void setTask(Task task) { put(KEY_TASK, task); }

    public ArrayList<String> getMembers() {
        return (ArrayList<String>) get(KEY_MEMBERS);
    }

    public void setMembers(ArrayList<String> members) {
        put(KEY_MEMBERS, members);
    }

    public ArrayList<String> getInvites() {
        return (ArrayList<String>) get(KEY_INVITES);
    }

    public void setInvites(ArrayList<String> invites) {
        put(KEY_INVITES, invites);
    }

    public static String getDateFormatted(Meetup meetup) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM d, y");
        String dateFormatted = dateFormat.format(meetup.getDate());
        return dateFormatted;
    }

    public static String getTimeFormatted(Meetup meetup) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeFormatted = timeFormat.format(meetup.getDate());
        return timeFormatted;
    }
}
