package com.example.meetplan.meetups;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.models.Meetup;
import com.parse.ParseUser;

import java.util.ArrayList;

/** Click listener in the meetups fragment that creates a new meetup
 * and adds the current user as a member. */
public class NewClickListener implements View.OnClickListener {

    /** Default name for a newly created meetup. */
    private static final String NEW_MEETUP_NAME = "New Meetup";

    /** Context of the meetups fragment. */
    private Context context;

    public NewClickListener(Context context) {
        this.context = context;
    }

    /** Creates a new meetup object with a default name and the current
     * user is set to a member of the meetup. Saves this created meetup and
     * directs to the details fragment for the meetup*/
    @Override
    public void onClick(View view) {
        ArrayList<String> memebers = new ArrayList<>();
        ArrayList<String> invites = new ArrayList<>();
        memebers.add(ParseUser.getCurrentUser().getUsername());
        Meetup meetup = new Meetup();
        meetup.setName(NEW_MEETUP_NAME);
        meetup.setMembers(memebers);
        meetup.setInvites(invites);
        meetup.saveInBackground();
        Fragment fragment = DetailsFragment.newInstance(meetup);
        ((MainActivity) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(MeetupsFragment.class.getSimpleName())
                .replace(R.id.flContainer, fragment)
                .commit();
    }
}
