package com.example.meetplan.details;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

/** Click listener for an item in the spinner dialog. When a user is clicked,
 * the invite callback saves the selected user as an invitee to the meetup.*/
public class InviteItemClick implements OnSpinerItemClick {

    /** View binding of the details fragment. */
    private FragmentDetailsBinding binding;

    /** List of usernames displayed on the spinner dialog. */
    private ArrayList<String> usernames;

    /** Selected meetup for which users are being invited. */
    private Meetup meetup;

    public InviteItemClick(FragmentDetailsBinding binding, ArrayList<String> usernames, Meetup meetup) {
        this.binding = binding;
        this.usernames = usernames;
        this.meetup = meetup;
    }

    /** When an item on the spinner dialog is clicked, it calls the invite call back. */
    @Override
    public void onClick(String item, int position) {
        InviteCallBack inviteCallBack = new InviteCallBack(binding, meetup, usernames.get(position));
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(inviteCallBack);
    }
}
