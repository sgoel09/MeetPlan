package com.example.meetplan.details;

import android.view.View;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SubmitDetailsClickListener implements View.OnClickListener {

    private Meetup meetup;
    FragmentDetailsBinding binding;

    SubmitDetailsClickListener(FragmentDetailsBinding binding, Meetup meetup) {
        this.binding = binding;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        saveMeetupInfo();
        changeToView();
        Snackbar.make(binding.getRoot(), "Changes saved", Snackbar.LENGTH_SHORT).show();
    }

    private void saveMeetupInfo() {
        String title = binding.titleEdit.getText().toString();
        String description = binding.descriptionEdit.getText().toString();
        meetup.setName(title);
        meetup.setDescription(description);
        meetup.saveInBackground();
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
}
