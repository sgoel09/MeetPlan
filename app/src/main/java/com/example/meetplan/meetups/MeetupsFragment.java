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
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Fragment to display the invited, upcoming, and if toggle on,
 * meetups of the current user. For the upcoming meetups, user can select
 * the meetup to view and edit details.
 */
public class MeetupsFragment extends Fragment {

    /** Tag for this fragment. */
    private static final String TAG = "MeetupsFragment";

    /** Key for the members of a meetup in the Parse database. */
    private static final String KEY_MEMBERS = "members";

    /** Key for the invites of a meetup in the Parse database. */
    private static final String KEY_INVITED = "invites";

    /** Key for the dates of a meetup in the Parse database. */
    private static final String KEY_DATE = "date";

    /** Title of the notifications. */
    private static final String NOTIFIACTION_TITLE = "MeetPlan";

    /** Text of the notification that reminders users about an upcoming meetup. */
    private static final String NOTIFICATION_TEXT = "You have an upcoming meetup: ";

    /** ID for the notification channel. */
    private static final String NOTIFICATION_CHANNEL_ID = "meetplanChannel";

    /** Name for the notification channel. */
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "meetplan";

    /** Description for the notification channel. */
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Reminders";

    /** Layout manager for the recyclerview of accepted meetups. */
    private LinearLayoutManager acceptedLayoutManager;

    /** Layout manager for the recyclerview of invited meetups. */
    private LinearLayoutManager invitedLayoutManager;

    /** Layout manager for the recyclerview of past meetups. */
    private LinearLayoutManager pastLayoutManager;

    /** Meetup adapter for the recyclerview of accepted meetups. */
    private MeetupAdapter acceptedAdapter;

    /** ImmutableList of meetups for the recyclerview of accepted meetups. */
    private ImmutableList<Meetup> acceptedMeetups;

    /** Meetup adapter for the recyclerview of invited meetups. */
    private MeetupAdapter invitedAdapter;

    /** ImmutableList of meetups for the recyclerview of invited meetups. */
    private ImmutableList<Meetup> invitedMeetups;

    /** Meetup adapter for the recyclerview of past meetups. */
    private MeetupAdapter pastAdapter;

    /** ImmutableList of meetups for the recyclerview of past meetups. */
    private ImmutableList<Meetup> pastMeetups;

    /** Click listener for creating a new meetup. */
    private NewClickListener newClickListener;

    /** View binding for this fragment. */
    private FragmentMeetupsBinding binding;

    /** Required empty public constructor. */
    public MeetupsFragment() {}

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

    /** Sets up all the recycler views and adapters, and the listeners for the new button and switch. */
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

    /** Sets the past switch initially to false and hides the views associated with past meetups. */
    private void setPastSwitch() {
        binding.pastSwitch.setChecked(false);
        binding.pastLabel.setVisibility(View.GONE);
        binding.pastRecyclerView.setVisibility(View.GONE);
    }

    /** Sets up the accepted meetups recyclerview by defining a layout manager, creating an adapter,
     * and binding to the recyclerview. */
    private void setUpAcceptedAdapter() {
        acceptedLayoutManager = new LinearLayoutManager(getContext());
        acceptedMeetups = ImmutableList.of();
        acceptedAdapter = new MeetupAdapter((Activity) getContext(), acceptedMeetups, false, getParentFragmentManager());
        binding.meetupsRecyclerView.setAdapter(acceptedAdapter);
        binding.meetupsRecyclerView.setLayoutManager(acceptedLayoutManager);
        queryAcceptedMeetups();
    }

    /** Sets up the invited meetups recyclerview by defining a layout manager, creating an adapter,
     * and binding to the recyclerview. */
    private void setUpInvitedAdapter() {
        invitedLayoutManager = new LinearLayoutManager(getContext());
        invitedMeetups = ImmutableList.of();
        invitedAdapter = new MeetupAdapter((Activity) getContext(), invitedMeetups, true, getParentFragmentManager());
        binding.invitedRecyclerView.setAdapter(invitedAdapter);
        binding.invitedRecyclerView.setLayoutManager(invitedLayoutManager);
        queryInvitedMeetups();
    }

    /** Sets up the past meetups recyclerview by defining a layout manager, creating an adapter,
     * and binding to the recyclerview. */
    private void setUpPastAdapter() {
        pastLayoutManager = new LinearLayoutManager(getContext());
        pastMeetups = ImmutableList.of();
        pastAdapter = new MeetupAdapter((Activity) getContext(), pastMeetups, false, getParentFragmentManager());
        binding.pastRecyclerView.setAdapter(pastAdapter);
        binding.pastRecyclerView.setLayoutManager(pastLayoutManager);
    }

    /** Queries all the past meetups of the current user by filtering meetups where the date of the meetup
     * is before the current date. */
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

    /** Queries all the invited meetups of the current user by filtering meetups where the user has an
     * invite to the meetup. */
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

    /** Queries all the past meetups of the current user by filtering meetups where the date of the meetup
     * is later the current date. */
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
                    Snackbar.make(binding.getRoot(), getString(R.string.meetups_error), BaseTransientBottomBar.LENGTH_SHORT).show();                    return;
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

    /** Determines if any upcoming meetup for the current user is within the next hour, and if so,
     * notifies the user by creating a new notification. */
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

    /** Creates a notification channel for a notification to be added to the notification manager. */
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

    /** Creates a notification for an upcoming meetup and notifies the notification manager.
     * @param name name of the upcoming meetup that is being notified about */
    private void createNotification(String name) {
        createNotificationChannel();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(NOTIFIACTION_TITLE)
                        .setContentText(NOTIFICATION_TEXT + name);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());
    }
}