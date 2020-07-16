package com.example.meetplan;

import android.view.View;

import com.example.meetplan.databinding.FragmentDetailsBinding;

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
    }

    private void saveMeetupInfo() {
        String title = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();
        meetup.setName(title);
        meetup.setDescription(description);
        meetup.saveInBackground();
    }

    private void changeToView() {
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvDescription.setVisibility(View.VISIBLE);
        binding.etTitle.setVisibility(View.GONE);
        binding.etDescription.setVisibility(View.GONE);
        binding.tvTitle.setText(meetup.getName());
        binding.tvDescription.setText(meetup.getDescription());
    }
}
