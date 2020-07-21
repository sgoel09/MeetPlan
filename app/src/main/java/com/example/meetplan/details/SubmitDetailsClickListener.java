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
        binding.title.setText(meetup.getName());
        binding.titleEdit.setVisibility(View.GONE);

        if (meetup.getDescription() != null && !meetup.getDescription().equals("")) {
            binding.description.setVisibility(View.VISIBLE);
            binding.description.setText(meetup.getDescription());
            binding.descriptionLabel.setVisibility(View.VISIBLE);
        } else {
            binding.description.setVisibility(View.GONE);
            binding.descriptionLabel.setVisibility(View.GONE);
        }
        binding.descriptionEdit.setVisibility(View.GONE);

        if (meetup.getDate() != null && !Meetup.getDateFormatted(meetup).equals("")) {
            binding.date.setVisibility(View.VISIBLE);
            binding.date.setText(Meetup.getDateFormatted(meetup));
            binding.dateLabel.setVisibility(View.VISIBLE);
        } else {
            binding.date.setVisibility(View.GONE);
            binding.dateLabel.setVisibility(View.GONE);
        }

        if (meetup.getDate() != null && !Meetup.getTimeFormatted(meetup).equals("")) {
            binding.time.setVisibility(View.VISIBLE);
            binding.time.setText(Meetup.getTimeFormatted(meetup));
            binding.timeLabel.setVisibility(View.VISIBLE);
        } else {
            binding.time.setVisibility(View.GONE);
            binding.timeLabel.setVisibility(View.GONE);
        }

        if (meetup.getTask() != null) {
            binding.activity.setVisibility(View.VISIBLE);
            binding.activityLabel.setVisibility(View.VISIBLE);
            binding.activity.setText(meetup.getTask().getName());
            binding.location.setVisibility(View.VISIBLE);
            binding.locationLabel.setVisibility(View.VISIBLE);
            binding.locationLabel.setText(meetup.getTask().getPlace());
        } else {
            binding.activity.setVisibility(View.GONE);
            binding.activityLabel.setVisibility(View.GONE);
            binding.location.setVisibility(View.GONE);
            binding.locationLabel.setVisibility(View.GONE);
        }

        binding.browseButton.setVisibility(View.GONE);
        binding.inviteDialogButton.setVisibility(View.GONE);
        binding.dateButton.setVisibility(View.GONE);
        binding.timeButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.submitButton.setVisibility(View.GONE);
    }
}
