package com.example.meetplan;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.meetplan.browse.BottomNavigationItemSelectedListener;
import com.example.meetplan.databinding.ActivityMainBinding;
import com.example.meetplan.databinding.NavHeaderBinding;
import com.example.meetplan.meetups.MeetupsFragment;
import com.example.meetplan.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Main activity in the MeetPlan app.
 * Holds the drawer navigation, bottom navigation, and fragment container.
 * */
public class MainActivity extends AppCompatActivity {

    public static int currentPosition;

    /** Fragment manager of this activity. */
    final FragmentManager fragmentManager = getSupportFragmentManager();

    /** Key for the user profile pic in the Parse database. */
    private static final String PROFILE_PIC_KEY = "profilepic";

    /** Key for the user name in the Parse database. */
    private static final String NAME_KEY = "name";

    /** View binding for the header of the drawer navigation. */
    private NavHeaderBinding navHeaderBinding;

    /** Layout for the drawer navigation. */
    private DrawerLayout drawerLayout;

    /** Toggle for the drawer navigation to open and close. */
    private ActionBarDrawerToggle toggle;

    /** Item selected listener for the bottom navigation in browsing tasks. */
    public BottomNavigationItemSelectedListener itemSelectedListener;

    /** View binding for this activity. */
    ActivityMainBinding binding;

    /** Sets the navigations, toolbar, and item selected listener for the activity. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        currentPosition = 0;

        View hview = binding.nv.getHeaderView(0);
        navHeaderBinding = NavHeaderBinding.bind(hview);

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();

        fragmentManager.beginTransaction().addToBackStack(getLocalClassName()).replace(R.id.flContainer, new MeetupsFragment()).commit();

        setUserInfo();

        binding.nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home:
                        fragment = new MeetupsFragment();
                        break;
                    case R.id.profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().addToBackStack(getLocalClassName()).replace(R.id.flContainer, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        itemSelectedListener = new BottomNavigationItemSelectedListener(getSupportFragmentManager());
        binding.bottomNavigation.setOnNavigationItemSelectedListener(itemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    /** Hides the keyboard on the event of a touch outside of the keyboard. */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Sets the visibility of the bottom navigation.
     * @param show boolean on whether to show the navigation or not.
     * */
    public void showBottomNavigation(Boolean show) {
        if (show) {
            binding.bottomNavigation.setVisibility(View.VISIBLE);
        } else {
            binding.bottomNavigation.setVisibility(View.GONE);
        }
    }

    /** Queries information about current user to update drawer navigation header. */
    protected void setUserInfo() {
        ParseUser user = ParseUser.getCurrentUser();
        navHeaderBinding.usernameLabel2.setText(user.getUsername());
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(PROFILE_PIC_KEY);
        query.setLimit(1);
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                ParseFile file = objects.get(0).getParseFile(PROFILE_PIC_KEY);
                Glide.with(MainActivity.this).load(file.getUrl()).circleCrop().into(navHeaderBinding.image);
                navHeaderBinding.name.setText(objects.get(0).getString(NAME_KEY));
            }
        });
    }
}