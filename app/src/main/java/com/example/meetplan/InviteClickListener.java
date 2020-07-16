package com.example.meetplan;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class InviteClickListener implements View.OnClickListener {

    private static final String TAG = "InviteClickListener";
    private AppCompatActivity activity;
    private Meetup meetup;
    private InviteCallBack inviteCallBack;
    FragmentDetailsBinding binding;

    InviteClickListener(AppCompatActivity activity, FragmentDetailsBinding binding, Meetup meetup) {
        this.activity = activity;
        this.binding = binding;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        String invitee = binding.etInvites.getText().toString();
        inviteMember(invitee);
    }

    private void inviteMember(final String invitee) {
        inviteCallBack = new InviteCallBack(activity, binding, meetup, invitee);
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(inviteCallBack);
    }
}
