package com.example.meetplan.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.transition.MaterialContainerTransform;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private static final int TRANSITION_DURATION = 500;
    private static final String KEY_MEETUP = "meetup";
    private static final String KEY_USERNAME = "username";
    private static final String DATE_FORMAT = "EE, MMM d, y";
    private static final String TIME_FORMAT = "h:mm a";
    private static final String NONE_STRING = "None";
    private DatePickerClickListener datePickerClickListener;
    private TimePickerClickListener timePickerClickListener;
    private EditDetailsClickListener editDetailsClickListener;
    private SubmitDetailsClickListener submitDetailsClickListener;
    private InviteItemClick inviteItemClick;
    private InviteClickListener inviteClickListener;
    private BrowseClickListener browseClickListener;
    private ExpenseClickListener expenseClickListener;
    private ArrayList<String> inviteUsernames = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    FragmentDetailsBinding binding;
    Meetup meetup;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Meetup meetup) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_OUT);
        transform.setDuration(TRANSITION_DURATION);
        setSharedElementEnterTransition(transform);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        ((MainActivity) getActivity()).showBottomNavigation(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);

        changeToView();
        displayMembers();
        dispalyInvites();
        setDateTime();

        updateInviteUsernames();

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), inviteUsernames, getString(R.string.invite_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        inviteItemClick = new InviteItemClick(binding, inviteUsernames, meetup);
        spinnerDialog.bindOnSpinerListener(inviteItemClick);

        inviteClickListener = new InviteClickListener(spinnerDialog);
        binding.inviteDialogButton.setOnClickListener(inviteClickListener);

        datePickerClickListener = new DatePickerClickListener(((MainActivity) getContext()).getSupportFragmentManager(), meetup);
        binding.dateButton.setOnClickListener(datePickerClickListener);

        timePickerClickListener = new TimePickerClickListener(((MainActivity) getContext()).getSupportFragmentManager(), meetup);
        binding.timeButton.setOnClickListener(timePickerClickListener);

        editDetailsClickListener = new EditDetailsClickListener(binding, meetup);
        binding.editButton.setOnClickListener(editDetailsClickListener);

        submitDetailsClickListener = new SubmitDetailsClickListener(binding, meetup);
        binding.submitButton.setOnClickListener(submitDetailsClickListener);

        browseClickListener = new BrowseClickListener(getContext(), meetup);
        binding.browseButton.setOnClickListener(browseClickListener);

        expenseClickListener = new ExpenseClickListener(getContext(), meetup);
        binding.expenseButton.setOnClickListener(expenseClickListener);
    }

    private void updateInviteUsernames() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (ParseUser user : objects) {
                    inviteUsernames.add(user.getUsername());
                }
            }
        });
    }

    private void setDateTime() {
        if (meetup.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String dateFormatted = dateFormat.format(meetup.getDate());
            binding.date.setText(dateFormatted);

            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeFormatted = timeFormat.format(meetup.getDate());
            binding.time.setText(timeFormatted);
        }
    }

    private void changeToView() {
        binding.title.setVisibility(View.VISIBLE);
        binding.title.setText(meetup.getName());
        binding.titleEdit.setVisibility(View.GONE);

        if (meetup.getDescription() != null && !meetup.getDescription().isEmpty()) {
            binding.description.setVisibility(View.VISIBLE);
            binding.description.setText(meetup.getDescription());
            binding.descriptionLabel.setVisibility(View.VISIBLE);
        } else {
            binding.description.setVisibility(View.GONE);
            binding.descriptionLabel.setVisibility(View.GONE);
        }
        binding.descriptionEdit.setVisibility(View.GONE);

        if (meetup.getDate() != null && !Meetup.getDateFormatted(meetup).isEmpty()) {
            binding.date.setVisibility(View.VISIBLE);
            binding.date.setText(Meetup.getDateFormatted(meetup));
            binding.dateLabel.setVisibility(View.VISIBLE);
        } else {
            binding.date.setVisibility(View.GONE);
            binding.dateLabel.setVisibility(View.GONE);
        }

        if (meetup.getDate() != null && !Meetup.getTimeFormatted(meetup).isEmpty()) {
            binding.time.setVisibility(View.VISIBLE);
            binding.time.setText(Meetup.getTimeFormatted(meetup));
            binding.timeLabel.setVisibility(View.VISIBLE);
        } else {
            binding.time.setVisibility(View.GONE);
            binding.timeLabel.setVisibility(View.GONE);
        }

        if (meetup.getTask() != null) {
            binding.activity.setVisibility(View.VISIBLE);
            binding.activityLabel.setVisibility(View.VISIBLE);
            binding.activity.setText(meetup.getTask().getName());
            binding.location.setVisibility(View.VISIBLE);
            binding.locationLabel.setVisibility(View.VISIBLE);
            binding.location.setText(meetup.getTask().getPlace());
            binding.address.setText(meetup.getTask().getAddress());
        } else {
            binding.activity.setVisibility(View.GONE);
            binding.activityLabel.setVisibility(View.GONE);
            binding.location.setVisibility(View.GONE);
            binding.locationLabel.setVisibility(View.GONE);
        }

        binding.browseButton.setVisibility(View.GONE);
        binding.inviteDialogButton.setVisibility(View.GONE);
        binding.dateButton.setVisibility(View.GONE);
        binding.timeButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.submitButton.setVisibility(View.GONE);
    }

    private void displayMembers() {
        ArrayList<String> members = meetup.getMembers();
        String allMembers = "";
        for (String username : members) {
            if (!members.get(members.size() - 1).equals(username)) {
                allMembers += String.format("%s; ", username);
            } else {
                allMembers += username;
            }
        }
        binding.members.setText(allMembers);
    }

    private void dispalyInvites() {
        ArrayList<String> invites = meetup.getInvites();
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