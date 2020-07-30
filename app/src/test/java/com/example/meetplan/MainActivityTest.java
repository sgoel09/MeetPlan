package com.example.meetplan;

import androidx.drawerlayout.widget.DrawerLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class MainActivityTest {

    MainActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void onNavigationItemSelected() {
        DrawerLayout drawerLayout = new DrawerLayout(Robolectric.buildActivity(MainActivity.class).create().get());
        DrawerLayout.DrawerListener mockDrawerListener = mock(DrawerLayout.DrawerListener.class);
        drawerLayout.setDrawerListener(mockDrawerListener);
    }
}
