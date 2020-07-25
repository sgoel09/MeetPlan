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

public class BottomNavigationItemSelectedListener
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentManager fragmentManager;
    Meetup meetup;
    String city;

    public BottomNavigationItemSelectedListener(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_events:
                fragment = EventFragment.newInstance(meetup, city);
                break;
            case R.id.action_restaurants:
                // fall through
            default:
                fragment = RestaurantFragment.newInstance(meetup, city);
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        return true;
    }

    public void addMeetup(Meetup meetup) {
        this.meetup = meetup;
    }

    public void addCity(String city) {
        this.city = city;
    }
}
