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

import java.io.File;

public class ProfileFragment extends Fragment implements PassNewPhoto {

    private FragmentProfileBinding binding;
    private static final int GALLERY_REQUEST_CODE = 20;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final String KEY_NAME = "name";
    private static final String KEY_PROFILE_PIC = "profilepic";
    private static final String KEY_OBJECT_ID = "objectId";
    private static final String TAG = "ProfileFragment";
    private static final String IMAGE_TYPE = "image/*";
    private static final String[] mimeTypes = {"image/jpeg", "image/png"};
    private ProfilePicCallBack profilePicCallBack;
    private ProfileFragment thisFragment = this;
    private LogoutClickListener logoutClickListener;
    private Snackbar snackbar;
    @Nullable
    private File file;
    @Nullable
    private ParseFile photoFile;
    private String photoFileName = "photo.jpg";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

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

        binding.changeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInfo();
            }
        });

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoFragment addPhotoBottomDialogFragment = new AddPhotoFragment();
                addPhotoBottomDialogFragment.setTargetFragment(thisFragment, 0);
                addPhotoBottomDialogFragment.show(((MainActivity) getContext()).getSupportFragmentManager(), "add_photo_dialog_fragment");
            }
        });
    }

    private void setUserInfo() {
        String username = binding.username.getText().toString();
        String email = binding.email.getText().toString();
        String name = binding.name.getText().toString();
        ParseUser user = ParseUser.getCurrentUser();
        if (!username.isEmpty()) {
            user.setUsername(username);
        }
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        if (!name.isEmpty()) {
            user.put(KEY_NAME, name);
        }
        user.saveInBackground();
        Snackbar.make(binding.getRoot(), getString(R.string.info_saved),BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FragmentProfileBinding getBinding() {
        return binding;
    }

    @Override
    public void passCreatedParseFile(ParseFile file) {
        snackbar = Snackbar.make(binding.getRoot(), R.string.image_load, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
        ParseUser user = ParseUser.getCurrentUser();
        user.put(KEY_PROFILE_PIC, file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                loadProfilePic();
                snackbar.dismiss();
            }
        });
    }
}