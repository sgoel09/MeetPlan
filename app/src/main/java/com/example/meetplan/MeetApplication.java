package com.example.meetplan;

import android.app.Application;

import com.example.meetplan.expenses.models.Expense;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.expenses.models.SplitExpense;
import com.example.meetplan.models.Task;
import com.parse.Parse;
import com.parse.ParseObject;

/** Application for the Parse databse used to store information in this
 * mobile app. Registers the models in this project used as classes in the database. */
public class MeetApplication extends Application {

    /** Sets the application id and server based on the values in the Heroku settings of the Parse database. */
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Meetup.class);
        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(Expense.class);
        ParseObject.registerSubclass(SplitExpense.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("shefali-meetplan")
                .clientKey(null)
                .server("https://shefali-meetplan.herokuapp.com/parse").build());
    }
}
