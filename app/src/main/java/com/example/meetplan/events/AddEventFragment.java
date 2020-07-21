package com.example.meetplan.events;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentAddEventBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends DialogFragment {

    private FragmentAddEventBinding binding;
    private Meetup meetup;
    private String name;
    private String place;
    private String address;
    private Task task;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance(Meetup meetup, String name, String place, String address) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", meetup);
        args.putString("name", name);
        args.putString("place", place);
        args.putString("address", address);
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
        binding = FragmentAddEventBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
        name = getArguments().getString("name");
        place = getArguments().getString("place");
        address = getArguments().getString("address");
        binding.activity.setText(name);
        binding.location.setText(place);
        binding.address.setText(address);
        binding.question.setText(String.format("%s %s?", getString(R.string.add_question), meetup.getName()));

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task();
                task.setName(name);
                task.setPlace(place);
                task.setAddress(address);
                task.saveInBackground();
                meetup.setTask(task);
                meetup.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Snackbar.make(binding.getRoot(), "Task saved", BaseTransientBottomBar.LENGTH_SHORT).show();
                                                dismiss();
                                            }
                                        });
            }
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}