package com.example.meetplan.details;

import android.view.View;

import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.models.Meetup;

public class EditDetailsClickListener implements View.OnClickListener {

    private Meetup meetup;
    FragmentDetailsBinding binding;

    EditDetailsClickListener(FragmentDetailsBinding binding, Meetup meetup) {
        this.binding = binding;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        changeToEdit();
    }

    private void changeToEdit() {
        binding.title.setVisibility(View.GONE);
        binding.description.setVisibility(View.GONE);
        binding.titleEdit.setVisibility(View.VISIBLE);
        binding.descriptionEdit.setVisibility(View.VISIBLE);
        binding.titleEdit.setText(meetup.getName());
        binding.descriptionEdit.setText(meetup.getDescription());
        binding.inviteDialogButton.setVisibility(View.VISIBLE);
        binding.dateButton.setVisibility(View.VISIBLE);
        binding.timeButton.setVisibility(View.VISIBLE);
        binding.editButton.setVisibility(View.GONE);
        binding.submitButton.setVisibility(View.VISIBLE);
    }
}
