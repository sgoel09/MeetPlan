package com.example.meetplan.details;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
            Snackbar.make(binding.getRoot(), "Issue with fetching users", BaseTransientBottomBar.LENGTH_SHORT).show();
            return;
        }
        if (userExists(users)) {
            ArrayList<String> invites = meetup.getInvites();
            invites.add(invitee);
            meetup.setInvites(invites);
            meetup.saveInBackground();
            Snackbar.make(binding.getRoot(), "User invited!", BaseTransientBottomBar.LENGTH_SHORT).show();
            displayMembers();
            return;
        }
    }

    private boolean userExists(List<ParseUser> users) {
        for (ParseUser user : users) {
            if (user.getUsername().equals(invitee)) {
                if (meetup.getMembers().contains(user.getUsername())) {
                    Snackbar.make(binding.getRoot(), "User is already a member", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return false;
                } else if (meetup.getInvites().contains(user.getUsername())) {
                    Snackbar.make(binding.getRoot(), "User is already invited", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        }
        Snackbar.make(binding.getRoot(), "User does not exist", BaseTransientBottomBar.LENGTH_SHORT).show();
        return false;
    }

    private void displayMembers() {
        List<String> members = meetup.getMembers();
        List<String> invites = meetup.getInvites();
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
        binding.members.setText(allMembers);
        binding.invites.setText(allInvites);
    }
}
