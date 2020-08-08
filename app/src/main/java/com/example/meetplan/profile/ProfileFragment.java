package com.example.meetplan.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.example.meetplan.gallery.AddPhotoFragment;
import com.example.meetplan.gallery.PassNewPhoto;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/** Fragment for the profile of the current user that displays
 * account information including name, username, and email.
 * Allows users to change their profile picture. */
public class ProfileFragment extends Fragment implements PassNewPhoto {

    /** Key for the name of the current user in the Parse database. */
    private static final String KEY_NAME = "name";

    /** Key for the profile picture of the current user in the Parse database. */
    private static final String KEY_PROFILE_PIC = "profilepic";

    /** Key for the object ID of the current user in the Parse database. */
    private static final String KEY_OBJECT_ID = "objectId";

    /** Tag of this fragment. */
    private static final String TAG = "ProfileFragment";

    /** Find callback for retrieving custom information of the current user. */
    private ProfilePicCallBack profilePicCallBack;

    /** View binding of this fragment. */
    private FragmentProfileBinding binding;

    /** Click listener to logout of the current account. */
    private LogoutClickListener logoutClickListener;

    /** Click listener to save inputted changes to the current account information. */
    private ChangeInfoClickListener changeInfoClickListener;

    private NewProfilePicCallBack newProfilePicCallBack;

    /** Snackbar to notify the user that a new profile picture is loading. */
    private Snackbar snackbar;

    /** Pointer of this fragment. */
    private ProfileFragment thisFragment = this;

    /** Required empty public constructor */
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    /** Sets user information and click listeners for the respective views. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadProfilePic();
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            binding.username.setHint(user.getUsername());
            binding.email.setHint(user.getEmail());
        }

        logoutClickListener = new LogoutClickListener(getContext());
        binding.logoutButton.setOnClickListener(logoutClickListener);

        changeInfoClickListener = new ChangeInfoClickListener(binding, getContext());
        binding.changeInfoButton.setOnClickListener(changeInfoClickListener);

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoFragment addPhotoBottomDialogFragment = new AddPhotoFragment();
                addPhotoBottomDialogFragment.setTargetFragment(thisFragment, 0);
                addPhotoBottomDialogFragment.show(((MainActivity) getContext()).getSupportFragmentManager(), TAG);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    /** Loads custom information of a ParseUser, including profile picture and name,
     * from the database by querying the current ParseUser to include that information. */
    public void loadProfilePic() {
        if (ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.include(KEY_PROFILE_PIC);
            query.include(KEY_NAME);
            query.setLimit(1);
            query.whereEqualTo(KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
            profilePicCallBack = new ProfilePicCallBack(getContext(), binding);
            query.findInBackground(profilePicCallBack);
        }
    }

    /** @return the view binding of this fragment, which is made available for unit testing. */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FragmentProfileBinding getBinding() {
        return binding;
    }

    /** Updates the new photo file that is passed in from the add photo dialog fragment to the user.
     * Saves the user in the Parse database and displays the new profile picture.
     * @param file ParseFile of the photo to be saved */
    @Override
    public void passCreatedParseFile(ParseFile file) {
        snackbar = Snackbar.make(binding.getRoot(), R.string.image_load, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
        ParseUser user = ParseUser.getCurrentUser();
        user.put(KEY_PROFILE_PIC, file);
        newProfilePicCallBack = new NewProfilePicCallBack(binding, getContext(), snackbar);
        user.saveInBackground(newProfilePicCallBack);
    }
}