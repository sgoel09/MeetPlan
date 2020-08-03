package com.example.meetplan.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetplan.LoginActivity;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.example.meetplan.gallery.AddPhotoFragment;
import com.example.meetplan.gallery.PassNewPhoto;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(username);
        user.setEmail(email);
        user.saveInBackground();
        Snackbar.make(binding.getRoot(), getString(R.string.info_saved),BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            uriToParse(selectedImageUri);
            saveProfilePic();
            binding.profilePic.setImageURI(selectedImageUri);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (file != null) {
                    Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                    saveProfilePic();
                    binding.profilePic.setImageBitmap(takenImage);
                }
            } else {
                Snackbar.make(binding.getRoot(), getString(R.string.take_pic_error),BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }
    }

    private void onLaunchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getPhotoFileUri(photoFileName);
        photoFile = new ParseFile(file);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovidermeetplan", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Snackbar.make(binding.getRoot(), getString(R.string.profile_pic_error), BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void saveProfilePic() {
        ParseUser user = ParseUser.getCurrentUser();
        user.put(KEY_PROFILE_PIC, photoFile);
        user.saveInBackground();
    }

    private void uriToParse(Uri selectedImageUri) {
        InputStream imageStream = null;
        try {
            imageStream = getContext().getContentResolver().openInputStream(selectedImageUri);
        } catch (FileNotFoundException e) {
            Snackbar.make(binding.getRoot(), getString(R.string.profile_pic_error), BaseTransientBottomBar.LENGTH_SHORT).show();
            return;
        }
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();
        photoFile = new ParseFile(bitmapBytes);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE_TYPE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
        ParseUser user = ParseUser.getCurrentUser();
        user.put(KEY_PROFILE_PIC, file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                loadProfilePic();
            }
        });
    }
}