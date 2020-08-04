package com.example.meetplan.details;

import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.meetups.MeetupsFragment;
import com.example.meetplan.models.Meetup;

public class EditDetailsClickListener implements View.OnClickListener {

    private Meetup meetup;
    private FragmentDetailsBinding binding;
    private FragmentManager fragmentManager;

    EditDetailsClickListener(FragmentDetailsBinding binding, Meetup meetup, FragmentManager fragmentManager) {
        this.binding = binding;
        this.meetup = meetup;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View view) {
        changeToEdit();
    }

    private void changeToEdit() {
        binding.title.setVisibility(View.VISIBLE);
        binding.description.setVisibility(View.VISIBLE);
        binding.title.setText(meetup.getName());
        binding.description.setText(meetup.getDescription());
        binding.descriptionLabel.setVisibility(View.VISIBLE);
        binding.dateLabel.setVisibility(View.VISIBLE);
        binding.timeLabel.setVisibility(View.VISIBLE);
        binding.date.setVisibility(View.VISIBLE);
        binding.time.setVisibility(View.VISIBLE);
        if (meetup.getDate() != null) {
            binding.date.setText(meetup.getDateFormatted(meetup));
            binding.time.setText(meetup.getTimeFormatted(meetup));
        }
        binding.location.setVisibility(View.VISIBLE);
        binding.locationLabel.setVisibility(View.VISIBLE);
        binding.activity.setVisibility(View.VISIBLE);
        binding.activityLabel.setVisibility(View.VISIBLE);
        if (meetup.getTask() != null) {
            binding.activity.setText(meetup.getTask().getName());
            binding.location.setText(meetup.getTask().getPlace());
            binding.address.setText(meetup.getTask().getAddress());
        }
        binding.browseButton.setVisibility(View.VISIBLE);
        binding.inviteDialogButton.setVisibility(View.VISIBLE);
        binding.dateButton.setVisibility(View.VISIBLE);
        binding.timeButton.setVisibility(View.VISIBLE);
        binding.editButton.setVisibility(View.GONE);
        binding.submitButton.setVisibility(View.VISIBLE);
        binding.galleryButton.setVisibility(View.GONE);
        binding.expenseButton.setVisibility(View.GONE);
        binding.titleButton.setVisibility(View.VISIBLE);
        binding.descriptionButton.setVisibility(View.VISIBLE);
    }
}
