package com.example.meetplan;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.meetplan.browse.addtask.AddTaskFragment;
import com.example.meetplan.models.Meetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class AddTaskFragmentTest {

    @Mock
    Meetup meetup;
    AddTaskFragment fragment;
    Bundle fragmentArgs;
    FragmentFactory factory;
    FragmentScenario<AddTaskFragment> scenario;

    @Before
    public void setUp() throws Exception {
        meetup = new Meetup();
        fragment = AddTaskFragment.newInstance(meetup, null, null, null);
        fragmentArgs = new Bundle();
        fragmentArgs.putParcelable("meetup", meetup);
        factory = new FragmentFactory();
        scenario = FragmentScenario.launch(AddTaskFragment.class, fragmentArgs, factory);
    }

    @Test
    public void checkNotNull() {
        assertNotNull(fragment);
    }

    @Test
    public void cancelFragmentButton() {
        scenario.onFragment(new CancelAction());
    }

    private static class CancelAction implements FragmentScenario.FragmentAction<AddTaskFragment> {

        ImageView closeIcon;

        @Override
        public void perform(@NonNull AddTaskFragment fragment) {
            closeIcon = fragment.getBinding().close;
            validateButton();
            closeIcon.performClick();
            shadowOf(getMainLooper()).idle();
            assert(!fragment.isVisible());
        }

        private void validateButton() {
            assertNotNull(closeIcon);
            assertEquals(View.VISIBLE, closeIcon.getVisibility());
        }
    }
}
