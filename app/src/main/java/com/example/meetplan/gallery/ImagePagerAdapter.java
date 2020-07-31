package com.example.meetplan.gallery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.meetplan.models.Meetup;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private Meetup meetup;

    public ImagePagerAdapter(Fragment fragment, Meetup meetup) {
        // Note: Initialize with the child fragment manager.
        super(fragment.getParentFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.meetup = meetup;
    }

    @Override
    public int getCount() {
        return meetup.getPicture().size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(meetup.getPicture().get(position));
    }
}