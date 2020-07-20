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
        binding.tvTitle.setVisibility(View.GONE);
        binding.tvDescription.setVisibility(View.GONE);
        binding.etTitle.setVisibility(View.VISIBLE);
        binding.etDescription.setVisibility(View.VISIBLE);
        binding.etTitle.setText(meetup.getName());
        binding.etDescription.setText(meetup.getDescription());
        binding.btnInvite.setVisibility(View.VISIBLE);
        binding.btnDate.setVisibility(View.VISIBLE);
        binding.btnTime.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnSubmit.setVisibility(View.VISIBLE);
    }
}
