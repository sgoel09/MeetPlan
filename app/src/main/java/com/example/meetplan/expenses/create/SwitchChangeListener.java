package com.example.meetplan.expenses.create;

import android.view.View;
import android.widget.CompoundButton;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;

public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

    private FragmentCreateExpenseBinding binding;

    public SwitchChangeListener(FragmentCreateExpenseBinding binding) {
        this.binding = binding;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            binding.membersButton.setVisibility(View.GONE);
        } else {
            binding.membersButton.setVisibility(View.VISIBLE);
        }
    }
}
