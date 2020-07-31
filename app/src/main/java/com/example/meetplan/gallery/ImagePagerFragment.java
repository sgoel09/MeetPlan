package com.example.meetplan.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.FragmentImagePagerBinding;
import com.example.meetplan.models.Meetup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagePagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagePagerFragment extends Fragment {

    private ViewPager viewPager;
    private FragmentImagePagerBinding binding;

    public ImagePagerFragment() {
        // Required empty public constructor
    }

    public static ImagePagerFragment newInstance(Meetup meetup) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", meetup);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentImagePagerBinding.inflate(getLayoutInflater(), container, false);
        viewPager = binding.viewPager;
        Meetup meetup = getArguments().getParcelable("meetup");
        viewPager.setAdapter(new ImagePagerAdapter(this, meetup));
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        viewPager.setCurrentItem(MainActivity.currentPosition);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MainActivity.currentPosition = position;
            }
        });
        return viewPager;
    }
}