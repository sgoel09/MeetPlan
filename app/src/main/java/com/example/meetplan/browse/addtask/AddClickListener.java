package com.example.meetplan.browse.addtask;

import android.view.View;

import com.example.meetplan.databinding.FragmentAddEventBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.SaveCallback;

/** Listener for the add button in the AddTaskFragment.
 * Creates a new task with the task information and saves it to the meetup.
 * */
public class AddClickListener implements View.OnClickListener {

    /** Inteface to dismiss the AddTaskFragment. */
    private FragmentDismisser dismisser;

    /** View binding for the AddTaskFragment. */
    private FragmentAddEventBinding binding;

    /** Meetup to add the task to. */
    private Meetup meetup;

    /** Name of the new task. */
    private String name;

    /** Place of the new task. */
    private String place;

    /** Address of the new task. */
    private String address;

    /** Constructor for the listener.
     * @param dismisser Inteface to dismiss the AddTaskFragment
     * @param binding View binding for the AddTaskFragment
     * @param meetup Meetup to add the task to
     * @param name Name of the new task
     * @param place Place of the new task
     * @param address Address of the new task
     * */
    public AddClickListener(FragmentDismisser dismisser, FragmentAddEventBinding binding,
                            Meetup meetup, String name, String place, String address) {
        this.binding = binding;
        this.meetup = meetup;
        this.name = name;
        this.place = place;
        this.address = address;
        this.dismisser = dismisser;
    }

    /**
     * On click of the add button, creates a new task with the task
     * information and saves it to the meetup.
     * */
    @Override
    public void onClick(View view) {
        Task task = new Task(name, place, address);
        task.saveInBackground();
        meetup.setTask(task);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Snackbar.make(binding.getRoot(), "Task saved", BaseTransientBottomBar.LENGTH_SHORT).show();
                dismisser.dismissFragment();
            }
        });
    }
}
