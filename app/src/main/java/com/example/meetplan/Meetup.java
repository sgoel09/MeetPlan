package com.example.meetplan;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Meetup")
public class Meetup extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_MEMBERS = "members";


    public Meetup() {}

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public ArrayList<String> getMembers() {
        return (ArrayList<String>) get(KEY_MEMBERS);
    }

    public void setMembers(ArrayList<String> members) {
        put(KEY_MEMBERS, members);
    }
}
