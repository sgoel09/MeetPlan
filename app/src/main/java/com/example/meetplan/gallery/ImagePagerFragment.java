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
 * Pager fragment that sets the transition animation between the small image view in the
 * recycler view to the the enlarged image view in the image fragment.
 * Sets a listener for the view pager to update photos when paging.
 */
public class ImagePagerFragment extends Fragment {

    /** View pager for this fragment. */
    private ViewPager viewPager;

    /** View binding for this fragment. */
    private FragmentImagePagerBinding binding;

    /** Required empty public constructor */
    public ImagePagerFragment() {}

    public static ImagePagerFragment newInstance(Meetup meetup) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putParcelable("meetup", meetup);
        fragment.setArguments(args);
        return fragment;
    }

    /** Sets the current position of the adapter and listener to update selection coordinator
     * while paging photos.*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentImagePagerBinding.inflate(getLayoutInflater(), container, false);
        viewPager = binding.viewPager;
        Meetup meetup = getArguments().getParcelable("meetup");
        viewPager.setAdapter(new ImagePagerAdapter(this, meetup));
        viewPager.setCurrentItem(MainActivity.currentPosition);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MainActivity.currentPosition = position;
            }
        });

        prepareSharedElementTransition();

        if (savedInstanceState == null) {
            postponeEnterTransition();
        }
        return viewPager;
    }

    /** Sets the enter transition to enlarge the shared element to an item in the recyclerview.
     * This transition is for a shared element transaction animation. */
    private void prepareSharedElementTransition() {
        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        setSharedElementEnterTransition(transition);

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
                sharedElements.put(names.get(0), view.findViewById(R.id.imageView));
            }
        });
    }
}