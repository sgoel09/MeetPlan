package com.example.meetplan.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import androidx.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentGalleryBinding;
import com.example.meetplan.databinding.FragmentMeetupsBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment implements PassNewPhoto {

    private static final String KEY_MEETUP = "meetup";
    private static final int GALLERY_REQUEST_CODE = 22;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 40;
    private static final String IMAGE_TYPE = "image/*";
    private static final String[] mimeTypes = {"image/jpeg", "image/png"};
    private static final String TAG = "GalleryFragment";
    private Meetup meetup;
    private GalleryFragment thisFragment = this;
    private GridLayoutManager layoutManager;
    private GalleryAdapter adapter;
    private ImmutableList<ParseFile> pictures;
    private FragmentGalleryBinding binding;
    @Nullable
    private File file;
    @Nullable
    private ParseFile photoFile;
    private String photoFileName = "photo.jpg";

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(Meetup meetup) {
        GalleryFragment fragment = new GalleryFragment();
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
        binding = FragmentGalleryBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        prepareTransitions();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);
        pictures = ImmutableList.<ParseFile>builder().addAll(meetup.getPicture()).build();
        layoutManager = new GridLayoutManager(getContext(),  3);
        adapter = new GalleryAdapter(getContext(), pictures, meetup);
        binding.picturesRecyclerView.setAdapter(adapter);
        binding.picturesRecyclerView.setLayoutManager(layoutManager);

//        binding.chooseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pickFromGallery();
//            }
//        });
//
//        binding.takeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onLaunchCamera();
//            }
//        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoFragment addPhotoBottomDialogFragment = AddPhotoFragment.newInstance(meetup);
                addPhotoBottomDialogFragment.setTargetFragment(thisFragment, 0);
                addPhotoBottomDialogFragment.show(((MainActivity) getContext()).getSupportFragmentManager(), "add_photo_dialog_fragment");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            uriToParse(selectedImageUri);
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
        ArrayList<ParseFile> picturesList = meetup.getPicture();
        picturesList.add(photoFile);
        meetup.put("pictures", picturesList);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                pictures = ImmutableList.<ParseFile>builder().addAll(meetup.getPicture()).build();
                adapter.updateData(pictures);
            }
        });
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
        saveNewPicture();
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

    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                //super.onMapSharedElements(names, sharedElements);
                RecyclerView.ViewHolder selectedViewHolder = binding.picturesRecyclerView
                        .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                if (selectedViewHolder == null) {
                    return;
                }

                // Map the first shared element name to the child ImageView.
                sharedElements
                        .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.picture));
            }
        });
    }

    @Override
    public void passCreatedParseFile(ParseFile file) {
        pictures = ImmutableList.<ParseFile>builder().addAll(meetup.getPicture()).build();
        adapter.updateData(pictures);
    }
}