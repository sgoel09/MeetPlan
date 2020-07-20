package com.example.meetplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetplan.databinding.ActivityMainBinding;
import com.example.meetplan.databinding.NavHeaderBinding;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.meetups.MeetupsFragment;
import com.example.meetplan.models.Meetup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "MainActivity";
    private NavHeaderBinding navHeaderBinding;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        View hview = binding.nv.getHeaderView(0);
        navHeaderBinding = NavHeaderBinding.bind(hview);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                    default:
                        fragment = new ProfileFragment();
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_compose) {
            ArrayList<String> memebers = new ArrayList<>();
            memebers.add(ParseUser.getCurrentUser().getUsername());
            Meetup meetup = new Meetup();
            meetup.setName("New Meetup");
            meetup.setMembers(memebers);
            meetup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, "Meetup was saved successfully");
                }
            });
            Fragment fragment = DetailsFragment.newInstance(meetup);
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setUserInfo() {
        ParseUser user = ParseUser.getCurrentUser();
        navHeaderBinding.tvUsername.setText(user.getUsername());
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("profilepic");
        query.setLimit(1);
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                ParseFile file = objects.get(0).getParseFile("profilepic");
                Glide.with(MainActivity.this).load(file.getUrl()).circleCrop().into(navHeaderBinding.ivImage);
                navHeaderBinding.tvName.setText(objects.get(0).getString("name"));
            }
        });
    }
}