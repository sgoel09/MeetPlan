package com.example.meetplan.details;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.browse.events.EventFragment;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.browse.restaurants.RestaurantFragment;

public class BrowseClickListener implements View.OnClickListener {

    private Context context;
    private Meetup meetup;

    public BrowseClickListener(Context context, Meetup meetup) {
        this.context = context;
        this.meetup = meetup;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = RestaurantFragment.newInstance(meetup, null);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(RestaurantFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();
    }
}
