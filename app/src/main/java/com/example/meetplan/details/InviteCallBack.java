package com.example.meetplan.details;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/** Call back after saving the new selected invites to the meetup
 * in the Parse database. */
public class InviteCallBack implements FindCallback<ParseUser> {

    private static final String NONE_STRING = "None";
    private Meetup meetup;
    FragmentDetailsBinding binding;
    private String invitee;

    InviteCallBack(FragmentDetailsBinding binding, Meetup meetup, String invitee) {
        this.meetup = meetup;
        this.binding = binding;
        this.invitee = invitee;
    }

    /** Verifies that the selected user exists, saves the updates to the meetup,
     * and changes the view for the list of members with the new information. */
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

    /** Iterates through all the parse users to ensure that the new invite is an existent user. */
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

    /** Displays the updated lists of members and invites on the views of the details fragment. */
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
        binding.members.setText(allMembers);
        String allInvites = "";
        if (invites != null && !invites.isEmpty()) {
            for (String username : invites) {
                if (!invites.get(invites.size() - 1).equals(username)) {
                    allInvites += String.format("%s; ", username);
                } else {
                    allInvites += username;
                }
            }
            binding.invites.setText(allInvites);
        } else {
            binding.invites.setText(NONE_STRING);
        }
    }
}
