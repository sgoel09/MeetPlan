package com.example.meetplan;

import android.content.Intent;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class LoginActivityTest {

    private LoginActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(LoginActivity.class);
    }

    @Test
    public void validateImageViewContent() {
        ImageView logo = (ImageView) activity.findViewById(R.id.logo);
        assertNotNull("Logo could not be found", logo);
    }

    @Test
    public void signupActivityStartedOnClick() {
        activity.findViewById(R.id.signupButton).performClick();

        Intent expectedIntent = new Intent(activity, SignupActivity.class);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}
