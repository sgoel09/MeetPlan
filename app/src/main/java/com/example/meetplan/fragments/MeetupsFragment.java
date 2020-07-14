package com.example.meetplan.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.Meetup;
import com.example.meetplan.MeetupAdapter;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentMeetupsBinding;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetupsFragment extends Fragment {

    private static final String KEY_MEMBERS = "members";
    private static final String TAG = "MeetupsFragment";
    private static final String KEY_INVITED = "invites";
    private LinearLayoutManager acceptedLayoutManager;
    private LinearLayoutManager invitedLayoutManager;
    private MeetupAdapter acceptedAdapter;
    private ImmutableList<Meetup> acceptedMeetups;
    private MeetupAdapter invitedAdapter;
    private ImmutableList<Meetup> invitedMeetups;
    FragmentMeetupsBinding binding;

    public MeetupsFragment() {
        // Required empty public constructor
    }

    public static MeetupsFragment newInstance(String param1, String param2) {
        MeetupsFragment fragment = new MeetupsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_meetups, container, false);
        binding = FragmentMeetupsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acceptedLayoutManager = new LinearLayoutManager(getContext());
        acceptedMeetups = ImmutableList.of();
        acceptedAdapter = new MeetupAdapter((Activity) getContext(), acceptedMeetups);
        binding.rvMeetups.setAdapter(acceptedAdapter);
        binding.rvMeetups.setLayoutManager(acceptedLayoutManager);
        queryAcceptedMeetups();

        invitedLayoutManager = new LinearLayoutManager(getContext());
        invitedMeetups = ImmutableList.of();
        invitedAdapter = new MeetupAdapter((Activity) getContext(), invitedMeetups);
        binding.rvInvited.setAdapter(invitedAdapter);
        binding.rvInvited.setLayoutManager(invitedLayoutManager);
        queryInvitedMeetups();
    }

    private void queryInvitedMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_INVITED, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Meetup>() {
            @Override
            public void done(List<Meetup> meetups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Meetup meetup : meetups) {
                    Log.i(TAG, "Meetup: " + meetup.getName());
                }
                invitedMeetups = ImmutableList.<Meetup>builder().addAll(meetups).build();
                invitedAdapter.updateData(invitedMeetups);
            }
        });
    }

    private void queryAcceptedMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_MEMBERS, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Meetup>() {
            @Override
            public void done(List<Meetup> meetups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Meetup meetup : meetups) {
                    Log.i(TAG, "Meetup: " + meetup.getName());
                }
                acceptedMeetups = ImmutableList.<Meetup>builder().addAll(meetups).build();
                acceptedAdapter.updateData(acceptedMeetups);
            }
        });
    }

}