package com.example.meetplan.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.addtask.AddTaskFragment;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.expenses.ExpenseFragment;
import com.example.meetplan.gallery.GalleryFragment;
import com.example.meetplan.models.Meetup;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements PassNewInfo {

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

    public static DetailsFragment newInstance(Meetup meetup, boolean resume) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putBoolean("resume", resume);
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getContext()).showBottomNavigation(false);

        meetup = getArguments().getParcelable(KEY_MEETUP);

        queryIncludes(meetup.getObjectId());

        if (getArguments().getBoolean("resume") == true) {
            changeToEdit();
        } else {
            changeToView();
            displayMembers();
            dispalyInvites();
            setDateTime();
            updateInviteUsernames();
        }

        spinnerDialog = new SpinnerDialog(getActivity(), inviteUsernames, getString(R.string.invite_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        inviteItemClick = new InviteItemClick(binding, inviteUsernames, meetup);
        spinnerDialog.bindOnSpinerListener(inviteItemClick);

        inviteClickListener = new InviteClickListener(spinnerDialog);
        binding.inviteDialogButton.setOnClickListener(inviteClickListener);

        datePickerClickListener = new DatePickerClickListener(getActivity().getSupportFragmentManager(), meetup);
        binding.dateButton.setOnClickListener(datePickerClickListener);

        timePickerClickListener = new TimePickerClickListener(getActivity().getSupportFragmentManager(), meetup);
        binding.timeButton.setOnClickListener(timePickerClickListener);

        editDetailsClickListener = new EditDetailsClickListener(binding, meetup, getActivity().getSupportFragmentManager());
        binding.editButton.setOnClickListener(editDetailsClickListener);

        submitDetailsClickListener = new SubmitDetailsClickListener(binding, meetup);
        binding.submitButton.setOnClickListener(submitDetailsClickListener);

        browseClickListener = new BrowseClickListener(getContext(), meetup);
        binding.browseButton.setOnClickListener(browseClickListener);

        expenseClickListener = new ExpenseClickListener(getActivity().getSupportFragmentManager(), getContext(), meetup);
        binding.expenseButton.setOnClickListener(expenseClickListener);

        binding.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFragment fragment = GalleryFragment.newInstance(meetup);
                getParentFragmentManager().beginTransaction().addToBackStack(DetailsFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();
            }
        });

        final FragmentManager fm = ((MainActivity) getContext()).getSupportFragmentManager();
        final Fragment thisFragment = this;
        binding.titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTitleFragment fragment = EditTitleFragment.newInstance(meetup, "Name");
                fragment.setTargetFragment(thisFragment, 0);
                fragment.show(fm, "fragment_edit_name");
            }
        });

        binding.descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                EditTitleFragment fragment = EditTitleFragment.newInstance(meetup, "Description");
                fragment.setTargetFragment(thisFragment, 0);
                fragment.show(fm, "fragment_edit_name");
            }
        });

    }

    private void queryIncludes(String objectId) {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereEqualTo("objectId", objectId);
        query.include("task");
        query.include("expenses");
        try {
            List<Meetup> objects  = query.find();
            if (objects.size() > 0) {
                meetup = objects.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void changeToEdit() {
        binding.title.setVisibility(View.VISIBLE);
        binding.description.setVisibility(View.VISIBLE);
        binding.title.setText(meetup.getName());
        binding.description.setText(meetup.getDescription());
        binding.descriptionLabel.setVisibility(View.VISIBLE);
        binding.dateLabel.setVisibility(View.VISIBLE);
        binding.timeLabel.setVisibility(View.VISIBLE);
        binding.date.setVisibility(View.VISIBLE);
        binding.time.setVisibility(View.VISIBLE);
        if (meetup.getDate() != null) {
            binding.date.setText(meetup.getDateFormatted(meetup));
            binding.time.setText(meetup.getTimeFormatted(meetup));
        }
        binding.location.setVisibility(View.VISIBLE);
        binding.locationLabel.setVisibility(View.VISIBLE);
        binding.activity.setVisibility(View.VISIBLE);
        binding.activityLabel.setVisibility(View.VISIBLE);
        if (meetup.getTask() != null) {
            binding.activity.setText(meetup.getTask().getName());
            binding.location.setText(meetup.getTask().getPlace());
            binding.address.setText(meetup.getTask().getAddress());
        }
        binding.browseButton.setVisibility(View.VISIBLE);
        binding.inviteDialogButton.setVisibility(View.VISIBLE);
        binding.dateButton.setVisibility(View.VISIBLE);
        binding.timeButton.setVisibility(View.VISIBLE);
        binding.editButton.setVisibility(View.GONE);
        binding.submitButton.setVisibility(View.VISIBLE);
        binding.galleryButton.setVisibility(View.GONE);
        binding.expenseButton.setVisibility(View.GONE);
        binding.titleButton.setVisibility(View.VISIBLE);
        binding.descriptionButton.setVisibility(View.VISIBLE);
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

        if (meetup.getDescription() != null && !meetup.getDescription().isEmpty()) {
            binding.description.setVisibility(View.VISIBLE);
            binding.description.setText(meetup.getDescription());
            binding.descriptionLabel.setVisibility(View.VISIBLE);
        } else {
            binding.description.setVisibility(View.GONE);
            binding.descriptionLabel.setVisibility(View.GONE);
        }

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
            binding.address.setVisibility(View.GONE);
        }

        binding.browseButton.setVisibility(View.GONE);
        binding.inviteDialogButton.setVisibility(View.GONE);
        binding.dateButton.setVisibility(View.GONE);
        binding.timeButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.submitButton.setVisibility(View.GONE);
        binding.titleButton.setVisibility(View.GONE);
        binding.descriptionButton.setVisibility(View.GONE);
    }

    private void displayMembers() {
        ArrayList<String> members = meetup.getMembers();
        String allMembers = "";
        if (members != null) {
            for (String username : members) {
                if (!members.get(members.size() - 1).equals(username)) {
                    allMembers += String.format("%s; ", username);
                } else {
                    allMembers += username;
                }
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

    @VisibleForTesting (otherwise = VisibleForTesting.PRIVATE)
    public FragmentDetailsBinding getBinding() {
        return binding;
    }

    @Override
    public void passMeetupInformation(String type, String info) {
        meetup.put(type, info);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                changeToEdit();
            }
        });
    }
}