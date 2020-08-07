package com.example.meetplan.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentEditTitleBinding;
import com.example.meetplan.models.Meetup;

/** Fragment with an option to change the meetup
 * title or description, depending from which view
 * the fragment is being intialized from. */
public class EditTitleFragment extends DialogFragment {

    /** Key for the meetup in the arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the type in the Parse database. */
    private static final String KEY_TYPE = "type";

    /** Constant part of the title of the fragment. */
    private static final String edit = "Edit";

    /** View binding for this fragment. */
    private FragmentEditTitleBinding binding;

    /** Key for the type of information being edited. */
    private String type;

    /** Required empty constructor. */
    public EditTitleFragment() {}

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
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Base_Theme_MyApp);
        binding = FragmentEditTitleBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    /** Gets information from arguments and sets the on click listener to save when
     * the changes are finished. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

    /** Passes the new, changed information through the interface
     * for the details fragment to save to its meetup. */
    private void saveMeetup() {
        if (!binding.infoField.getText().toString().isEmpty()) {
            PassNewInfo mHost = (PassNewInfo) this.getTargetFragment();
            mHost.passMeetupInformation(type.toLowerCase(), binding.infoField.getText().toString());
        }
        dismiss();
    }
}