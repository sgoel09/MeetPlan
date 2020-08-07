package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.restaurants.RestaurantFragment;
import com.example.meetplan.models.Meetup;

/** Click listener for the browse button that directs to the
 * browse restaurants and events fragments. */
public class BrowseClickListener implements View.OnClickListener {

    /** Context of the details fragment. */
    private Context context;

    /** Selected meetup for which the restaurant/event is being browsed for. */
    private Meetup meetup;

    public BrowseClickListener(Context context, Meetup meetup) {
        this.context = context;
        this.meetup = meetup;
    }

    /** On click, creates a new instance of the restaurant fragment and
     * begins transaction to it. */
    @Override
    public void onClick(View view) {
        Fragment fragment = RestaurantFragment.newInstance(meetup, null);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(RestaurantFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();
    }
}
