package com.example.meetplan.meetups;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentMeetupsBinding;
import com.example.meetplan.expenses.create.SwitchChangeListener;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetupsFragment extends Fragment {

    private static final String KEY_MEMBERS = "members";
    private static final String TAG = "MeetupsFragment";
    private static final String KEY_INVITED = "invites";
    private static final String KEY_DATE = "date";
    private static final String NOTIFIACTION_TITLE = "MeetPlan";
    private static final String NOTIFICATION_TEXT = "You have an upcoming meetup: ";
    private static final String NOTIFICATION_CHANNEL_ID = "meetplanChannel";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "meetplan";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Reminders";
    private LinearLayoutManager acceptedLayoutManager;
    private LinearLayoutManager invitedLayoutManager;
    private LinearLayoutManager pastLayoutManager;
    private MeetupAdapter acceptedAdapter;
    private ImmutableList<Meetup> acceptedMeetups;
    private MeetupAdapter invitedAdapter;
    private ImmutableList<Meetup> invitedMeetups;
    private MeetupAdapter pastAdapter;
    private ImmutableList<Meetup> pastMeetups;
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
        setUpPastAdapter();
        setPastSwitch();
        newClickListener = new NewClickListener(getContext());
        binding.newButton.setOnClickListener(newClickListener);
        binding.pastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    binding.pastLabel.setVisibility(View.VISIBLE);
                    binding.pastRecyclerView.setVisibility(View.VISIBLE);
                    queryPastMeetups();
                } else {
                    binding.pastLabel.setVisibility(View.GONE);
                    binding.pastRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setPastSwitch() {
        binding.pastSwitch.setChecked(false);
        binding.pastLabel.setVisibility(View.GONE);
        binding.pastRecyclerView.setVisibility(View.GONE);
    }

    private void setUpAcceptedAdapter() {
        acceptedLayoutManager = new LinearLayoutManager(getContext());
        acceptedMeetups = ImmutableList.of();
        acceptedAdapter = new MeetupAdapter((Activity) getContext(), acceptedMeetups, false, getParentFragmentManager());
        binding.meetupsRecyclerView.setAdapter(acceptedAdapter);
        binding.meetupsRecyclerView.setLayoutManager(acceptedLayoutManager);
        queryAcceptedMeetups();
    }

    private void setUpInvitedAdapter() {
        invitedLayoutManager = new LinearLayoutManager(getContext());
        invitedMeetups = ImmutableList.of();
        invitedAdapter = new MeetupAdapter((Activity) getContext(), invitedMeetups, true, getParentFragmentManager());
        binding.invitedRecyclerView.setAdapter(invitedAdapter);
        binding.invitedRecyclerView.setLayoutManager(invitedLayoutManager);
        queryInvitedMeetups();
    }

    private void setUpPastAdapter() {
        pastLayoutManager = new LinearLayoutManager(getContext());
        pastMeetups = ImmutableList.of();
        pastAdapter = new MeetupAdapter((Activity) getContext(), pastMeetups, false, getParentFragmentManager());
        binding.pastRecyclerView.setAdapter(pastAdapter);
        binding.pastRecyclerView.setLayoutManager(pastLayoutManager);
    }

    private void queryPastMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_MEMBERS, ParseUser.getCurrentUser().getUsername());
        query.whereLessThan(KEY_DATE, new Date());
        query.orderByAscending(KEY_DATE);
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
                pastMeetups = ImmutableList.<Meetup>builder().addAll(meetups).build();
                pastAdapter.updateData(pastMeetups);
                if (pastMeetups.isEmpty()) {
                    binding.pastLabel.setVisibility(View.GONE);
                } else {
                    binding.pastLabel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void queryInvitedMeetups() {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereContains(KEY_INVITED, ParseUser.getCurrentUser().getUsername());
        query.orderByAscending(KEY_DATE);
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
        ParseQuery<Meetup> firstQuery = ParseQuery.getQuery(Meetup.class);
        firstQuery.whereContains(KEY_MEMBERS, ParseUser.getCurrentUser().getUsername());
        firstQuery.whereGreaterThanOrEqualTo(KEY_DATE, new Date());
        ParseQuery<Meetup> secondQuerry = ParseQuery.getQuery(Meetup.class);
        secondQuerry.whereEqualTo(KEY_DATE, null);
        List<ParseQuery<Meetup>> queries = new ArrayList<>();
        queries.add(firstQuery);
        queries.add(secondQuerry);
        ParseQuery<Meetup> query = ParseQuery.or(queries);
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
                queryUpcomingSoon();
            }
        });
    }

    private void queryUpcomingSoon() {
        Date compareDate = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        for (Meetup meetup : acceptedMeetups) {
            if (meetup.getDate() != null && meetup.getDate().compareTo(compareDate) < 0 && meetup.getNotified() == false) {
                createNotification(meetup.getName());
                meetup.setNotified(true);
                meetup.saveInBackground();
            }
        }
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification(String name) {
        createNotificationChannel();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.defaultprofilepic)
                        .setContentTitle(NOTIFIACTION_TITLE)
                        .setContentText(NOTIFICATION_TEXT + name);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());
    }
}