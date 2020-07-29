package com.example.meetplan;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.meetplan.profile.ProfileFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class ProfileFragmentTest {

    @Mock
    ProfileFragment fragment;

    @Before
    public void setUp() throws Exception {
        FragmentScenario.launch(ProfileFragment.class);
    }

    @Test
    public void checkNotNull() {
        fragment = new ProfileFragment();
        doNothing().when(fragment).loadProfilePic();
        assertNotNull(fragment);
    }

}
