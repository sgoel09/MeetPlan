package com.example.meetplan.browse.addtask;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentAddEventBinding;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;

/**
 * Fragment to add a task to a meetup.
 * Displays the task in a dialog fragment and asks user to add the fragment to the selected meetup.
 * */
public class AddTaskFragment extends DialogFragment implements FragmentDismisser {

    /** Key for the meetup of the task in the arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the name of the task in the arguments. */
    private static final String KEY_NAME = "name";

    /** Key for the place of the task in the arguments. */
    private static final String KEY_PLACE = "place";

    /** Key for the addresss of the task in the arguments. */
    private static final String KEY_ADDRESS = "address";

    /** View binding for this fragment. */
    private FragmentAddEventBinding binding;

    /** Meetup of the task. */
    private Meetup meetup;

    /** Name of the task. */
    private String name;

    /** Place of the task. */
    private String place;

    /** Address of the task. */
    private String address;

    /** Click listener for the add button to add the task. */
    private AddClickListener addClickListener;

    /** Click listener for the close button to close the fragment. */
    private CloseClickListener closeClickListener;

    /** Required empty public constructor. */
    public AddTaskFragment() {}

    /** Creates a new instance of the fragment and puts the task information in arguments.
     * @param meetup selected meetup for the task
     * @param name name of the task
     * @param place place of the task
     * @param address address of the task
     * */
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
        binding = FragmentAddEventBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    /** Get information from the arguments to set views and on click listeners. */
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FragmentAddEventBinding getBinding() {
        return binding;
    }
}