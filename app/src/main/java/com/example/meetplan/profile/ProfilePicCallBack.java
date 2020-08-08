package com.example.meetplan.profile;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

/** Find callback for querying the current user to include their
 * profile picture. Once done, loads the profile picture into its view. */
public class ProfilePicCallBack implements FindCallback<ParseUser> {

    /** Key for the profile picture of the current user in the Parse database. */
    private static final String KEY_PROFILE_PIC = "profilepic";

    /** Key for the name of the current user in the Parse database. */
    private static final String KEY_NAME = "name";

    /** View binding of the profile fragment. */
    private FragmentProfileBinding binding;

    /** Context of the profile fragment. */
    private Context context;

    public ProfilePicCallBack(Context context, FragmentProfileBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    /** Sets the views of the profile image and name with the information queried about
     * the current user. */
    @Override
    public void done(List<ParseUser> objects, ParseException e) {
        ParseFile file = objects.get(0).getParseFile(KEY_PROFILE_PIC);
        Glide.with(context).load(file.getUrl()).circleCrop().into(binding.profilePic);
        binding.name.setHint(objects.get(0).getString(KEY_NAME));
    }
}
