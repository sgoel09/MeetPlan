package com.example.meetplan.gallery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentGalleryBinding;
import com.example.meetplan.databinding.FragmentImageBinding;
import com.parse.ParseFile;

public class ImageFragment extends Fragment {

    private static final String KEY_IMAGE_RES = "image";
    private FragmentImageBinding binding;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(ParseFile parseFile) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putParcelable(KEY_IMAGE_RES, parseFile);
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        binding.imageView.setTransitionName(String.valueOf(getArguments().getParcelable(KEY_IMAGE_RES)));
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        ParseFile imageFile = arguments.getParcelable(KEY_IMAGE_RES);
        Glide.with(this).load(imageFile.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                            target, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.imageView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.menu_main, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        ShareActionProvider miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }
}