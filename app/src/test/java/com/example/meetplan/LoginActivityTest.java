package com.example.meetplan;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
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
    public void shouldNotBeNull() {
        assertNotNull(activity);
    }

    @Test
    public void validateImageViewContent() {
        ImageView logo = (ImageView) activity.findViewById(R.id.logo);
        assertNotNull("Logo could not be found", logo);
    }

    @Test
    public void shouldHaveLoginButton() {
        Button loginButton = (Button) activity.findViewById(R.id.loginButton);
        assertNotNull(loginButton);
        assertEquals(View.VISIBLE, loginButton.getVisibility());
        assertEquals(activity.getString(R.string.login), loginButton.getText());
    }

    @Test
    public void shouldHaveSignupButton() {
        Button signupButton = (Button) activity.findViewById(R.id.signupButton);
        assertNotNull(signupButton);
        assertEquals(View.VISIBLE, signupButton.getVisibility());
        assertEquals(activity.getString(R.string.signup), signupButton.getText());
    }

    @Test
    public void signupActivityStartedOnClick() {
        activity.findViewById(R.id.signupButton).performClick();

        Intent expectedIntent = new Intent(activity, SignupActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(expectedIntent.filterEquals(actualIntent));
    }
}
