package com.example.meetplan.meetups;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.meetplan.R;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.models.Meetup;
import com.google.android.material.transition.MaterialContainerTransform;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class NewClickListener implements View.OnClickListener {


    private static final String TAG = "NewClickListener";
    private AppCompatActivity context;

    public NewClickListener(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        ArrayList<String> memebers = new ArrayList<>();
        ArrayList<String> invites = new ArrayList<>();
        memebers.add(ParseUser.getCurrentUser().getUsername());
        Meetup meetup = new Meetup();
        meetup.setName("New Meetup");
        meetup.setMembers(memebers);
        meetup.setInvites(invites);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    return;
                }
                Log.i(TAG, "Meetup was saved successfully");
            }
        });
        Fragment fragment = DetailsFragment.newInstance(meetup);
        context.getSupportFragmentManager().beginTransaction().addSharedElement(view, "shared_element_container").replace(R.id.flContainer, fragment).addToBackStack("TAG").commit();
    }
}
