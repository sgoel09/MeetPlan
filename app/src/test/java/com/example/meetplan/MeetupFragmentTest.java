package com.example.meetplan;

import com.example.meetplan.meetups.MeetupsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class MeetupFragmentTest {

    MeetupsFragment fragment;

    @Before
    public void setUp() throws Exception {
        fragment = new MeetupsFragment();
    }

    @Test
    public void checkNotNull() {
        assertNotNull(fragment);
    }
}
