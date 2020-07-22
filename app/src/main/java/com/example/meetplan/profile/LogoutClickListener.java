package com.example.meetplan.profile;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.meetplan.LoginActivity;
import com.parse.ParseUser;

public class LogoutClickListener implements View.OnClickListener {

    private Context context;

    public LogoutClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
