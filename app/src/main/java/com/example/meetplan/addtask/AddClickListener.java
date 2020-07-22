package com.example.meetplan.addtask;

import android.view.View;

import com.example.meetplan.addtask.AddTaskFragment;
import com.example.meetplan.databinding.FragmentAddEventBinding;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.models.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddClickListener implements View.OnClickListener {

    private AddTaskFragment fragment;
    private FragmentAddEventBinding binding;
    private Meetup meetup;
    private String name;
    private String place;
    private String address;

    public AddClickListener(AddTaskFragment fragment, FragmentAddEventBinding binding, Meetup meetup, String name, String place, String address) {
        this.binding = binding;
        this.meetup = meetup;
        this.name = name;
        this.place = place;
        this.address = address;
        this.fragment = fragment;
    }

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
                fragment.dismiss();
            }
        });
    }
}
