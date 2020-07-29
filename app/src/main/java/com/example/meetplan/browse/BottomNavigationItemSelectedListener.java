package com.example.meetplan.browse;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetplan.R;
import com.example.meetplan.browse.events.EventFragment;
import com.example.meetplan.models.Meetup;
import com.example.meetplan.browse.restaurants.RestaurantFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Item selected listener for the bottom navigation.
 * Switches between different browse task fragments when the respected item is selected.
 * */
public class BottomNavigationItemSelectedListener
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    /** Fragment manager of the main activity. */
    private final FragmentManager fragmentManager;

    /** Selected meetup for which a task is browsed. */
    Meetup meetup;

    /** Last city searched from any task. */
    String city;

    /**
     * Constructor for this item selected listener.
     * @param fragmentManager fragment manager of the main activity
     * **/
    public BottomNavigationItemSelectedListener(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * Depending on the item selected, create a new instance of that fragment.
     * @param item item in the bottom navigation that is selected
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_events:
                fragment = EventFragment.newInstance(meetup, city);
                break;
            case R.id.action_restaurants:
            default:
                fragment = RestaurantFragment.newInstance(meetup, city);
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        return true;
    }

    /** Sets the local variable of the inputted meetup.
     * @param meetup meetup that the user is browsing for
     * */
    public void addMeetup(Meetup meetup) {
        this.meetup = meetup;
    }

    /**
     * Sets the local varaible of the inputted city.
     * @param city city that is browsed
     * */
    public void addCity(String city) {
        this.city = city;
    }
}
