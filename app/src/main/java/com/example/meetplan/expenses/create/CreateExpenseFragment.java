package com.example.meetplan.expenses.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentCreateExpenseBinding;
import com.example.meetplan.models.Meetup;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateExpenseFragment extends DialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private FragmentCreateExpenseBinding binding;
    private SpinnerDialog spinnerDialog;
    private Meetup meetup;
    private AddMemberClickListener addMemberClickListener;
    private SwitchChangeListener switchChangeListener;
    private SpinnerItemClick spinnerItemClick;

    public CreateExpenseFragment() {
        // Required empty public constructor
    }

    public static CreateExpenseFragment newInstance(Meetup meetup) {
        CreateExpenseFragment fragment = new CreateExpenseFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
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
        binding = FragmentCreateExpenseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);

        spinnerDialog = new SpinnerDialog((MainActivity) getContext(), meetup.getMembers(), getString(R.string.add_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);

        binding.allSwitch.setChecked(true);
        binding.membersButton.setVisibility(View.GONE);
        switchChangeListener = new SwitchChangeListener(binding);
        binding.allSwitch.setOnCheckedChangeListener(switchChangeListener);

        addMemberClickListener = new AddMemberClickListener(spinnerDialog);
        binding.membersButton.setOnClickListener(addMemberClickListener);

        spinnerItemClick = new SpinnerItemClick();
        spinnerDialog.bindOnSpinerListener(spinnerItemClick);
    }
}