package com.example.meetplan.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.events.EventFragment;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.MaterialContainerTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private DatePickerClickListener datePickerClickListener;
    private TimePickerClickListener timePickerClickListener;
    private EditDetailsClickListener editDetailsClickListener;
    private SubmitDetailsClickListener submitDetailsClickListener;
    private InviteClickListener inviteClickListener;
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
        transform.setDuration(500);
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

        datePickerClickListener = new DatePickerClickListener((MainActivity) getContext(), meetup);
        binding.btnDate.setOnClickListener(datePickerClickListener);

        timePickerClickListener = new TimePickerClickListener((MainActivity) getContext(), meetup);
        binding.btnTime.setOnClickListener(timePickerClickListener);

        editDetailsClickListener = new EditDetailsClickListener(binding, meetup);
        binding.btnEdit.setOnClickListener(editDetailsClickListener);

        submitDetailsClickListener = new SubmitDetailsClickListener(binding, meetup);
        binding.btnSubmit.setOnClickListener(submitDetailsClickListener);

        inviteClickListener = new InviteClickListener((MainActivity) getContext(), binding, meetup);
        binding.btnInvite.setOnClickListener(inviteClickListener);

        binding.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EventFragment();
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
    }

    private void setDateTime() {
        if (meetup.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM d, y");
            String dateFormatted = dateFormat.format(meetup.getDate());
            binding.tvDate.setText(dateFormatted);

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            String timeFormatted = timeFormat.format(meetup.getDate());
            binding.tvTime.setText(timeFormatted);
        }
    }

    private void changeToView() {
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvDescription.setVisibility(View.VISIBLE);
        binding.etTitle.setVisibility(View.GONE);
        binding.etDescription.setVisibility(View.GONE);
        binding.tvTitle.setText(meetup.getName());
        binding.tvDescription.setText(meetup.getDescription());
        binding.btnInvite.setVisibility(View.GONE);
        binding.btnDate.setVisibility(View.GONE);
        binding.btnTime.setVisibility(View.GONE);
        binding.btnEdit.setVisibility(View.VISIBLE);
        binding.btnSubmit.setVisibility(View.GONE);
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
}