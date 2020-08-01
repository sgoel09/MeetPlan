package com.example.meetplan.gallery;

import android.os.Bundle;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentImagePagerBinding;
import com.example.meetplan.models.Meetup;

import java.util.List;
import java.util.Map;

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

        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        double i = transition.getDuration();
        //setSharedElementEnterTransition(transition);
        //setEnterTransition(transition);
        prepareSharedElementTransition();

        if (savedInstanceState == null) {
            postponeEnterTransition();
        }
        return viewPager;
    }

    private void prepareSharedElementTransition() {
        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        setSharedElementEnterTransition(transition);

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Fragment currentFragment = (Fragment) viewPager.getAdapter()
                        .instantiateItem(viewPager, MainActivity.currentPosition);
                View view = currentFragment.getView();
                if (view == null) {
                    return;
                }

                // Map the first shared element name to the child ImageView.
                sharedElements.put(names.get(0), view.findViewById(R.id.imageView));
            }
        });
    }
}