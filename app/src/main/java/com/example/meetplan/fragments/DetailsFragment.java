package com.example.meetplan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.meetplan.DatePickerFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.Meetup;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false);
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        changeToView();
        displayMembers();
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToEdit();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeetupInfo();
                changeToView();
            }
        });
        binding.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invitee = binding.etInvites.getText().toString();
                inviteMember(invitee);
            }
        });
        binding.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(meetup);
                newFragment.show(((MainActivity) getContext()).getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (meetup.getDate() != null) {
            binding.tvDate.setText(meetup.getDate().toString());
        }
    }

    private void changeToEdit() {
        binding.tvTitle.setVisibility(View.GONE);
        binding.tvDescription.setVisibility(View.GONE);
        binding.etTitle.setVisibility(View.VISIBLE);
        binding.etDescription.setVisibility(View.VISIBLE);
        binding.etTitle.setText(meetup.getName());
        binding.etDescription.setText(meetup.getDescription());
    }

    private void changeToView() {
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvDescription.setVisibility(View.VISIBLE);
        binding.etTitle.setVisibility(View.GONE);
        binding.etDescription.setVisibility(View.GONE);
        binding.tvTitle.setText(meetup.getName());
        binding.tvDescription.setText(meetup.getDescription());
    }

    private void saveMeetupInfo() {
        String title = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();
        meetup.setName(title);
        meetup.setDescription(description);
        meetup.saveInBackground();
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
        binding.tvMembers.setText(allMembers);
        binding.tvInvites.setText(allInvites);
    }

    private void inviteMember(final String invitee) {
        final Boolean[] exists = {false};
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }
                for (ParseUser user : users) {
                    if (user.getUsername().equals(invitee)) {
                        if (meetup.getMembers().contains(user.getUsername())) {
                            Toast.makeText(getContext(), "User is already a member", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (meetup.getMembers().contains(user.getUsername())) {
                            Toast.makeText(getContext(), "User is already invited", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        exists[0] = true;
                        ArrayList<String> invites = new ArrayList<>();
                        invites.add(invitee);
                        meetup.setInvites(invites);
                        meetup.saveInBackground();
                        Toast.makeText(getContext(), "User invited!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(getContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDate(Calendar c) {
        binding.tvDate.setText(c.getTime().toString());
    }
}