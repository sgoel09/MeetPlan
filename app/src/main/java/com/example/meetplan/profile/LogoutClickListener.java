package com.example.meetplan.profile;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.meetplan.LoginActivity;
import com.parse.ParseUser;

/** Click listener for the current user to logout of their account
 * and go back to the login screen. */
public class LogoutClickListener implements View.OnClickListener {

    /** Context for the profile fragment. */
    private Context context;

    public LogoutClickListener(Context context) {
        this.context = context;
    }

    /** Logs out the parse user and starts the intent for the login activity. */
    @Override
    public void onClick(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
