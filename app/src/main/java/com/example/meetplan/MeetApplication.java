package com.example.meetplan;

import android.app.Application;

import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Task;
import com.parse.Parse;
import com.parse.ParseObject;

public class MeetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Meetup.class);
        ParseObject.registerSubclass(Task.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("shefali-meetplan") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://shefali-meetplan.herokuapp.com/parse").build());
    }
}
