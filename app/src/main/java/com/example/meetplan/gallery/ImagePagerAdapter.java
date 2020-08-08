package com.example.meetplan.gallery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.meetplan.models.Meetup;

/** State pager adapter for the photos of a gallery.
 * Contains data for the photos so that it updates and loads the fragment
 * for the next photo when the user scrolls horizontally to the left and right. */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    /** Meetup for which the photos are being displayed. */
    private Meetup meetup;

    public ImagePagerAdapter(Fragment fragment, Meetup meetup) {
        super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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