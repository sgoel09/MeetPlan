package com.example.meetplan.browse.addtask;

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
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends DialogFragment implements FragmentDismisser {

    private static final String KEY_MEETUP = "meetup";
    private static final String KEY_NAME = "name";
    private static final String KEY_PLACE = "place";
    private static final String KEY_ADDRESS = "address";
    private FragmentAddEventBinding binding;
    private Meetup meetup;
    private String name;
    private String place;
    private String address;
    private AddClickListener addClickListener;
    private CloseClickListener closeClickListener;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    public static AddTaskFragment newInstance(Meetup meetup, String name, String place, String address) {
        AddTaskFragment fragment = new AddTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putString(KEY_NAME, name);
        args.putString(KEY_PLACE, place);
        args.putString(KEY_ADDRESS, address);
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
        meetup = getArguments().getParcelable(KEY_MEETUP);
        name = getArguments().getString(KEY_NAME);
        place = getArguments().getString(KEY_PLACE);
        address = getArguments().getString(KEY_ADDRESS);
        binding.activity.setText(name);
        binding.location.setText(place);
        binding.address.setText(address);
        binding.question.setText(String.format("%s %s?", getString(R.string.add_question), meetup.getName()));

        addClickListener = new AddClickListener(this, binding, meetup, name, place, address);
        binding.addButton.setOnClickListener(addClickListener);

        closeClickListener = new CloseClickListener(this);
        binding.close.setOnClickListener(closeClickListener);
    }

    @Override
    public void dismissFragment() {
        dismiss();
    }
}