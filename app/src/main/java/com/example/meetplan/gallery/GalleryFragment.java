package com.example.meetplan.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentGalleryBinding;
import com.example.meetplan.models.Meetup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Fragment that displays the photos of a meetup.
 * Has option to add a new photo to the meetup, select and view
 * an image larger, and share the image externally. */
public class GalleryFragment extends Fragment implements PassNewPhoto {

    /** Key for the meetup in the fragment arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Selected meetup for which photos are being displayed. */
    private Meetup meetup;

    /** Pointer to this fragment. */
    private GalleryFragment thisFragment = this;

    /** Layout manager for the recyclerview of photos. */
    private GridLayoutManager layoutManager;

    /** Photo adapter for the recyclerview of photos. */
    private GalleryAdapter adapter;

    /** ImmutableList of photos for the recyclerview of photos. */
    private ImmutableList<ParseFile> pictures;

    /** View binding for this fragment. */
    private FragmentGalleryBinding binding;

    /** Required empty public constructor. */
    public GalleryFragment() {}

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
        binding = FragmentGalleryBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        prepareTransitions();
        return view;
    }

    /** Sets up the adapter for photos and sets on the click listener for adding a new photo. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        meetup = getArguments().getParcelable(KEY_MEETUP);
        pictures = ImmutableList.<ParseFile>builder().addAll(meetup.getPicture()).build();
        layoutManager = new GridLayoutManager(getContext(),  3);
        adapter = new GalleryAdapter(getContext(), pictures, meetup);
        binding.picturesRecyclerView.setAdapter(adapter);
        binding.picturesRecyclerView.setLayoutManager(layoutManager);

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoFragment addPhotoBottomDialogFragment = AddPhotoFragment.newInstance(meetup);
                addPhotoBottomDialogFragment.setTargetFragment(thisFragment, 0);
                addPhotoBottomDialogFragment.show(((MainActivity) getContext()).getSupportFragmentManager(), "add_photo_dialog_fragment");
            }
        });
    }

    /** Saves the ParseFile as a new photo for the meetup. Once saved, updates the adapter for the image
     * to be displayed in the recyclerview.
     * @param file ParseFile for the new photo to be added */
    private void saveNewPicture(ParseFile file) {
        final Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.image_load), BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
        ArrayList<ParseFile> picturesList = meetup.getPicture();
        picturesList.add(file);
        meetup.put("pictures", picturesList);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                pictures = ImmutableList.<ParseFile>builder().addAll(meetup.getPicture()).build();
                adapter.updateData(pictures);
                snackbar.dismiss();
            }
        });
    }

    /** Sets the exit transition to a fade and the shared element to an item in the recyclerview.
     * This transition is for a shared element transaction animation. */
    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                RecyclerView.ViewHolder selectedViewHolder = binding.picturesRecyclerView
                        .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                if (selectedViewHolder == null) {
                    return;
                }
                sharedElements
                        .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.picture));
            }
        });
    }

    /** Saves the new photo file that is passed in from the add photo dialog fragment.
     * @param file ParseFile of the photo to be saved */
    @Override
    public void passCreatedParseFile(ParseFile file) {
        saveNewPicture(file);
    }
}