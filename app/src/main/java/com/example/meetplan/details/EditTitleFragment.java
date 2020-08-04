package com.example.meetplan.details;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.databinding.FragmentEditTitleBinding;
import com.example.meetplan.expenses.PassExpense;
import com.example.meetplan.models.Meetup;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class EditTitleFragment extends DialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private static final String KEY_TYPE = "type";
    private static final String edit = "Edit";
    private FragmentEditTitleBinding binding;
    private String type;
    private Meetup meetup;

    public EditTitleFragment() {
        // Required empty public constructor
    }

    public static EditTitleFragment newInstance(Meetup meetup, String type) {
        EditTitleFragment fragment = new EditTitleFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putString(KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Base_Theme_MyApp);
        binding = FragmentEditTitleBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);
        type = getArguments().getString(KEY_TYPE);
        binding.label.setText(String.format("%s %s", edit, type));
        binding.infoField.setHint(type.toLowerCase());

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeetup();
            }
        });
    }

    private void saveMeetup() {
        if (!binding.infoField.getText().toString().isEmpty()) {
            PassNewInfo mHost = (PassNewInfo) this.getTargetFragment();
            mHost.passMeetupInformation(type.toLowerCase(), binding.infoField.getText().toString());
        }
        dismiss();
    }
}