package com.example.meetplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.meetplan.profile.ProfileFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class ProfileFragmentTest {

    ProfileFragment fragment;
    Bundle fragmentArgs;
    FragmentFactory factory;
    FragmentScenario scenario;

    @Before
    public void setUp() throws Exception {
        fragment = new ProfileFragment();
        fragmentArgs = new Bundle();
        factory = new FragmentFactory();
        scenario = FragmentScenario.launch(ProfileFragment.class, fragmentArgs, factory);
    }

    @Test
    public void checkNotNull() {
        assertNotNull(fragment);
    }

    @Test
    public void testOptionButtons() {
        scenario.onFragment(new OptionButtonsAction());
    }

    @Test
    public void testLogoutIntent() {
        scenario.onFragment(new LogoutAction());
    }

    private static class OptionButtonsAction implements FragmentScenario.FragmentAction<ProfileFragment> {

        Button submitButton;
        Button captureButton;
        Button chooseButton;

        @Override
        public void perform(@NonNull ProfileFragment fragment) {
            submitButton = fragment.getBinding().captureButton;
            captureButton = fragment.getBinding().logoutButton;
            chooseButton = fragment.getBinding().chooseButton;

            checkValidity(submitButton);
            checkValidity(captureButton);
            checkValidity(chooseButton);

            submitButton.performClick();
            assert(fragment.getBinding().username.getText().toString().isEmpty());
        }

        private void checkValidity(Button button) {
            assertNotNull(button);
            assertEquals(View.VISIBLE, button.getVisibility());
        }
    }

    private static class LogoutAction implements FragmentScenario.FragmentAction<ProfileFragment> {

        Button logoutButton;

        @Override
        public void perform(@NonNull ProfileFragment fragment) {
            logoutButton = fragment.getBinding().logoutButton;
            assertNotNull(logoutButton);
            assertEquals(View.VISIBLE, logoutButton.getVisibility());

            logoutButton.performClick();
            Activity activity = fragment.getActivity();
            Intent expectedIntent = new Intent(activity, LoginActivity.class);
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();
            assertTrue(expectedIntent.filterEquals(actualIntent));
        }
    }
}
