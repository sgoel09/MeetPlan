package com.example.meetplan.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentAddPhotoBinding;
import com.example.meetplan.databinding.FragmentGalleryBinding;
import com.example.meetplan.expenses.PassExpense;
import com.example.meetplan.models.Meetup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPhotoFragment extends BottomSheetDialogFragment {

    private static final String KEY_MEETUP = "meetup";
    private static final int GALLERY_REQUEST_CODE = 22;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 40;
    private static final String IMAGE_TYPE = "image/*";
    private static final String[] mimeTypes = {"image/jpeg", "image/png"};
    private static final String TAG = "GalleryFragment";
    private Meetup meetup;
    private FragmentAddPhotoBinding binding;
    private AddPhotoFragment thisFragment = this;
    @Nullable
    private File file;
    @Nullable
    private ParseFile photoFile;
    private String photoFileName = "photo.jpg";

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    public static AddPhotoFragment newInstance(Meetup meetup) {
        AddPhotoFragment fragment = new AddPhotoFragment();
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
        binding = FragmentAddPhotoBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            meetup = getArguments().getParcelable(KEY_MEETUP);
        }

        binding.takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        binding.chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            uriToParse(selectedImageUri);
            saveNewPicture();
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (file != null) {
                    saveNewPicture();
                }
            } else {
                Snackbar.make(binding.getRoot(), getString(R.string.take_pic_error), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }
    }

    private void saveNewPicture() {
        dismiss();
        PassNewPhoto mHost = (PassNewPhoto) thisFragment.getTargetFragment();
        mHost.passCreatedParseFile(photoFile);
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
        photoFile =  new ParseFile(bitmapBytes);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE_TYPE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
}