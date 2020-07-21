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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends DialogFragment {

    private FragmentAddEventBinding binding;
    private Meetup meetup;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance(Meetup meetup) {
        AddEventFragment fragment = new AddEventFragment();
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
        binding = FragmentAddEventBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable("meetup");
    }
}