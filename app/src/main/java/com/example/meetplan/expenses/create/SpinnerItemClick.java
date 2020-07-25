package com.example.meetplan.expenses.create;

import com.example.meetplan.databinding.FragmentCreateExpenseBinding;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class SpinnerItemClick implements OnSpinerItemClick {

    private final FragmentCreateExpenseBinding binding;
    private ArrayList<String> users;

    public SpinnerItemClick(FragmentCreateExpenseBinding binding, ArrayList<String> users) {
        this.binding = binding;
        this.users = users;
    }

    @Override
    public void onClick(String item, int position) {
        users.add(item);
        String usersSoFar = binding.members.getText().toString();
        if (usersSoFar.equals("")) {
            usersSoFar = item;
        } else {
            usersSoFar += String.format("; %s", item);
        }
        binding.members.setText(usersSoFar);
    }
}
