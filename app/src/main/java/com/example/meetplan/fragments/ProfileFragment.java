package com.example.meetplan.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetplan.LoginActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final int GALLERY_REQUEST_CODE = 20;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final String TAG = "ProfileFragment";
    private File file;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("profilepic");
        query.setLimit(1);
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                ParseFile file = objects.get(0).getParseFile("profilepic");
                Glide.with(getContext()).load(file.getUrl()).circleCrop().into(binding.ivProfilePic);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        binding.btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            uriToParse(selectedImageUri);
            saveProfilePic();
            binding.ivProfilePic.setImageURI(selectedImageUri);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                saveProfilePic();
                binding.ivProfilePic.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
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
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void saveProfilePic() {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilepic", photoFile);
        user.saveInBackground();
    }

    private void uriToParse(Uri selectedImageUri) {
        InputStream imageStream = null;
        try {
            imageStream = getContext().getContentResolver().openInputStream(selectedImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();
        photoFile = new ParseFile(bitmapBytes);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
}