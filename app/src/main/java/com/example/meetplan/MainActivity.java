package com.example.meetplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.meetplan.browse.BottomNavigationItemSelectedListener;
import com.example.meetplan.databinding.ActivityMainBinding;
import com.example.meetplan.databinding.NavHeaderBinding;
import com.example.meetplan.meetups.MeetupsFragment;
import com.example.meetplan.profile.ProfileFragment;
import com.example.meetplan.utilities.TabSelectedListener;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "MainActivity";
    private static final String PROFILE_PIC_KEY = "profilepic";
    private static final String OBJECT_ID_KEY = "objectId";
    private static final String NAME_KEY = "name";
    private NavHeaderBinding navHeaderBinding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    public BottomNavigationItemSelectedListener itemSelectedListener;
    private TabSelectedListener tabSelectedListener;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        View hview = binding.nv.getHeaderView(0);
        navHeaderBinding = NavHeaderBinding.bind(hview);

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();

        fragmentManager.beginTransaction().replace(R.id.flContainer, new MeetupsFragment()).commit();

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
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
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

    public void showBottomNavigation(Boolean show) {
        if (show) {
            binding.bottomNavigation.setVisibility(View.VISIBLE);
        } else {
            binding.bottomNavigation.setVisibility(View.GONE);
        }
    }

    private void setUserInfo() {
        ParseUser user = ParseUser.getCurrentUser();
        navHeaderBinding.usernameLabel2.setText(user.getUsername());
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(PROFILE_PIC_KEY);
        query.setLimit(1);
        query.whereEqualTo(OBJECT_ID_KEY, ParseUser.getCurrentUser().getObjectId());
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