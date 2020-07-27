package com.example.meetplan.meetups;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.FragmentMeetupsBinding;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    private NewClickListener newClickListener;
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
        ((MainActivity) getActivity()).showBottomNavigation(false);
        setUpAcceptedAdapter();
        setUpInvitedAdapter();
        newClickListener = new NewClickListener((MainActivity) getContext());
        binding.newButton.setOnClickListener(newClickListener);
    }

    private void setUpAcceptedAdapter() {
        acceptedLayoutManager = new LinearLayoutManager(getContext());
        acceptedMeetups = ImmutableList.of();
        acceptedAdapter = new MeetupAdapter((Activity) getContext(), acceptedMeetups, false);
        binding.meetupsRecyclerView.setAdapter(acceptedAdapter);
        binding.meetupsRecyclerView.setLayoutManager(acceptedLayoutManager);
        queryAcceptedMeetups();
    }

    private void setUpInvitedAdapter() {
        invitedLayoutManager = new LinearLayoutManager(getContext());
        invitedMeetups = ImmutableList.of();
        invitedAdapter = new MeetupAdapter((Activity) getContext(), invitedMeetups, true);
        binding.invitedRecyclerView.setAdapter(invitedAdapter);
        binding.invitedRecyclerView.setLayoutManager(invitedLayoutManager);
        queryInvitedMeetups();
    }

    private void queryInvitedMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_INVITED, ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("date");
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
                if (invitedMeetups.isEmpty()) {
                    binding.invitedLabel.setVisibility(View.GONE);
                } else {
                    binding.invitedLabel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void queryAcceptedMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_MEMBERS, ParseUser.getCurrentUser().getUsername());
        query.include("task");
        query.include("expenses");
        query.orderByAscending("date");
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