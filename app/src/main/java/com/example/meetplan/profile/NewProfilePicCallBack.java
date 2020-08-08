package com.example.meetplan.profile;

import android.content.Context;

import com.example.meetplan.databinding.FragmentProfileBinding;
import com.example.meetplan.databinding.NavHeaderBinding;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/** Callback for saving the new profile picture.
 * Once done, loads the profile picture into its view. */
public class NewProfilePicCallBack implements SaveCallback {

    /** Key for the profile picture of the current user in the Parse database. */
    private static final String KEY_PROFILE_PIC = "profilepic";

    /** Key for the name of the current user in the Parse database. */
    private static final String KEY_NAME = "name";

    /** Key for the object ID of the current user in the Parse database. */
    private static final String KEY_OBJECT_ID = "objectId";

    /** Find callback for retrieving custom information of the current user. */
    private ProfilePicCallBack profilePicCallBack;

    /** Snackbar to notify the user that a new profile picture is loading. */
    private Snackbar snackbar;

    /** Context for the profile fragment. */
    private Context context;

    /** View binding for the profile fragment. */
    private FragmentProfileBinding binding;

    public NewProfilePicCallBack(FragmentProfileBinding binding, Context context, Snackbar snackbar) {
        this.binding = binding;
        this.context = context;
        this.snackbar = snackbar;
    }

    @Override
    public void done(ParseException e) {
        loadProfilePic();
        snackbar.dismiss();
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
            profilePicCallBack = new ProfilePicCallBack(context, binding);
            query.findInBackground(profilePicCallBack);
        }
    }
}
