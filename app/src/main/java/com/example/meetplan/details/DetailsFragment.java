package com.example.meetplan.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetplan.events.EventFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.transition.MaterialContainerTransform;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.widget.Toast.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private static final int TRANSITION_DURATION = 500;
    private DatePickerClickListener datePickerClickListener;
    private TimePickerClickListener timePickerClickListener;
    private EditDetailsClickListener editDetailsClickListener;
    private SubmitDetailsClickListener submitDetailsClickListener;
    private InviteItemClick inviteItemClick;
    private InviteClickListener inviteClickListener;
    private ArrayList<String> usernames = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    FragmentDetailsBinding binding;
    Meetup meetup;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Meetup meetup) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", meetup);
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
        meetup = getArguments().getParcelable("meetup");

        changeToView();
        displayMembers();
        setDateTime();

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (ParseUser user : objects) {
                    usernames.add(user.getUsername());
                }
            }
        });

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), usernames, "Select user to invite","Cancel");
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        inviteItemClick = new InviteItemClick((MainActivity) getContext(), binding, usernames, meetup);
        spinnerDialog.bindOnSpinerListener(inviteItemClick);

        inviteClickListener = new InviteClickListener(spinnerDialog);
        binding.inviteDialogButton.setOnClickListener(inviteClickListener);

        datePickerClickListener = new DatePickerClickListener((MainActivity) getContext(), meetup);
        binding.dateButton.setOnClickListener(datePickerClickListener);

        timePickerClickListener = new TimePickerClickListener((MainActivity) getContext(), meetup);
        binding.timeButton.setOnClickListener(timePickerClickListener);

        editDetailsClickListener = new EditDetailsClickListener(binding, meetup);
        binding.editButton.setOnClickListener(editDetailsClickListener);

        submitDetailsClickListener = new SubmitDetailsClickListener(binding, meetup);
        binding.submitButton.setOnClickListener(submitDetailsClickListener);

        binding.browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = EventFragment.newInstance(meetup);
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
    }

    private void setDateTime() {
        if (meetup.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM d, y");
            String dateFormatted = dateFormat.format(meetup.getDate());
            binding.date.setText(dateFormatted);

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            String timeFormatted = timeFormat.format(meetup.getDate());
            binding.time.setText(timeFormatted);
        }
    }

    private void changeToView() {
        binding.title.setVisibility(View.VISIBLE);
        binding.description.setVisibility(View.VISIBLE);
        binding.titleEdit.setVisibility(View.GONE);
        binding.descriptionEdit.setVisibility(View.GONE);
        binding.title.setText(meetup.getName());
        binding.description.setText(meetup.getDescription());
        binding.inviteDialogButton.setVisibility(View.GONE);
        binding.dateButton.setVisibility(View.GONE);
        binding.timeButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.submitButton.setVisibility(View.GONE);
    }

    private void displayMembers() {
        ArrayList<String> members = meetup.getMembers();
        ArrayList<String> invites = meetup.getInvites();
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