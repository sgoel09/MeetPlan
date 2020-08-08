package com.example.meetplan.profile;

import android.content.Context;
import android.view.View;

import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;

public class ChangeInfoClickListener implements View.OnClickListener {

    /** Key for the name of the current user in the Parse database. */
    private static final String KEY_NAME = "name";

    /** View binding of the profile fragment. */
    private FragmentProfileBinding binding;

    /** Context of the profile fragment. */
    private Context context;

    public ChangeInfoClickListener(FragmentProfileBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        setUserInfo();
    }

    /** Updates user information, including name, username, and email from the text views,
     * if changed. Saves the user in the Parse database. */
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
        Snackbar.make(binding.getRoot(), context.getString(R.string.info_saved), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
