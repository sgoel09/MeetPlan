package com.example.meetplan.details;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class InviteItemClick implements OnSpinerItemClick {

    private AppCompatActivity context;
    private FragmentDetailsBinding binding;
    private ArrayList<String> usernames;
    private Meetup meetup;

    public InviteItemClick(AppCompatActivity context, FragmentDetailsBinding binding, ArrayList<String> usernames, Meetup meetup) {
        this.context = context;
        this.binding = binding;
        this.usernames = usernames;
        this.meetup = meetup;
    }

    @Override
    public void onClick(String item, int position) {
        InviteCallBack inviteCallBack = new InviteCallBack((MainActivity)context, binding, meetup, usernames.get(position));
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(inviteCallBack);
    }
}
