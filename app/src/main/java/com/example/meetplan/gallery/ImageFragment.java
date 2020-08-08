package com.example.meetplan.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentImageBinding;
import com.parse.ParseFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** Fragment for a single images that enlarges the image to full screen.
 * Called when a single image in the gallery recyclerview is clicked. */
public class ImageFragment extends Fragment {

    /** Key for the image in the fragment arguments. */
    private static final String KEY_IMAGE_RES = "image";

    /** Image type for the selected photo. */
    private static final String IMAGE_TYPE = "image/*";

    /** File provider authority of this application. */
    private static final String AUTHORITY = "com.codepath.fileprovidermeetplan";

    /** Constant prefix of the child from the external file directory. */
    private static final String CHILD = "share_image_";

    /** Type of photo that is being shared. */
    private static final String PNG_TYPE = ".png";

    /** View binding for this fragment. */
    private FragmentImageBinding binding;

    /** Action provider to share the photo via text message, Google Drive, and multiple other options. */
    private ShareActionProvider miShareAction;

    /** Intent to share the photo. */
    private Intent shareIntent;

    /** Required empty public constructor. */
    public ImageFragment() {}

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
        binding = FragmentImageBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        binding.imageView.setTransitionName(String.valueOf(getArguments().getParcelable(KEY_IMAGE_RES)));
        setHasOptionsMenu(true);
        return view;
    }

    /** Loads the image into the large image view, while starting the shared element transaction animation.
     * Prepares the share intent action of the item for when menu item is selected. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        ParseFile imageFile = arguments.getParcelable(KEY_IMAGE_RES);
        Glide.with(this).load(imageFile.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                            target, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        prepareShareIntent(((BitmapDrawable) resource).getBitmap());
                        attachShareIntentAction();
                        return false;
                    }
                })
                .into(binding.imageView);
    }

    /** Attaches the share intent to the share action provider. */
    private void attachShareIntentAction() {
        if (miShareAction != null && shareIntent != null)
            miShareAction.setShareIntent(shareIntent);
    }

    /** Prepares the share intent by putting in the photo that will be shared.
     * @param bitmap Bitmap representation of the photo to be shared */
    private void prepareShareIntent(Bitmap bitmap) {
        Uri bmpUri = getBitmapFromDrawable(bitmap);
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType(IMAGE_TYPE);
    }

    /**
     * Gets the file from the the external directory.
     * @param bmp Bitmap representation for the photo that will be returned
     * */
    private Uri getBitmapFromDrawable(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            File file =  new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    CHILD + System.currentTimeMillis() + PNG_TYPE);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(getContext(), AUTHORITY, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        attachShareIntentAction();
    }
}