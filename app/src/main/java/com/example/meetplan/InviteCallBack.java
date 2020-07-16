package com.example.meetplan;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class InviteCallBack implements FindCallback<ParseUser> {

    private static final String TAG = "InviteCallBack";
    private Meetup meetup;
    private AppCompatActivity activity;
    FragmentDetailsBinding binding;
    private String invitee;

    InviteCallBack(AppCompatActivity activity, FragmentDetailsBinding binding, Meetup meetup, String invitee) {
        this.meetup = meetup;
        this.binding = binding;
        this.activity = activity;
        this.invitee = invitee;
    }

    @Override
    public void done(List<ParseUser> users, ParseException e) {
        if (e != null) {
            Log.e(TAG, "Issue with getting users", e);
            return;
        }
        for (ParseUser user : users) {
            if (user.getUsername().equals(invitee)) {
                if (meetup.getMembers().contains(user.getUsername())) {
                    Toast.makeText(activity, "User is already a member", Toast.LENGTH_SHORT).show();
                    return;
                } else if (meetup.getMembers().contains(user.getUsername())) {
                    Toast.makeText(activity, "User is already invited", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> invites = meetup.getInvites();
                invites.add(invitee);
                meetup.setInvites(invites);
                meetup.saveInBackground();
                Toast.makeText(activity, "User invited!", Toast.LENGTH_SHORT).show();
                displayMembers();
                return;
            }
        }
        Toast.makeText(activity, "Username does not exist", Toast.LENGTH_SHORT).show();
    }

    private void displayMembers() {
        ArrayList<String> members = meetup.getMembers();
        ArrayList<String> invites = meetup.getInvites();
        String allMembers = "";
        for (String username : members) {
            if (!members.get(members.size() - 1).equals(username)) {
                allMembers += String.format("%s; ", username);
            } else {
                allMembers += username;
            }
        }
        String allInvites = "";
        if (invites != null || invites.size() != 0) {
            for (String username : invites) {
                if (!invites.get(invites.size() - 1).equals(username)) {
                    allInvites += String.format("%s; ", username);
                } else {
                    allInvites += username;
                }
            }
        }
        binding.tvMembers.setText(allMembers);
        binding.tvInvites.setText(allInvites);
    }
}
