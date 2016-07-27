package com.example.koba.reklappclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Koba on 16/07/2016.
 */
public class AppActivity extends AppCompatActivity {

    public static int currentFragmentId;
    private String TITLES[] = {"მთავარი","ინფო","მომხმარებლის გვერდი"};
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout drawer;

    private ActionBarDrawerToggle mDrawerToggle;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ReklApp");

        nextDayListener();

        currentFragmentId = GlobalVariables.YOUTUBE_FRAGMENT_ID;

        if (getIntent().hasExtra("user")) {
            user = getIntent().getParcelableExtra("user");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(TITLES, user.name + " " + user.surname, user.mobile_number, R.drawable.logo);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                if (currentFragmentId != position) {
                    currentFragmentId = position;
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment fragment = getFragmentById(position, user);

                    if (fragment != null) {
                        manager.beginTransaction()
                                .replace(R.id.flContent, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
                drawer.closeDrawer(Gravity.LEFT);
            }
        }));

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        }; // Drawer Toggle Object Made
        drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();


        Fragment current = getFragmentById(currentFragmentId, user);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.flContent, current)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public Fragment getFragmentById(int id, User user) {
        Fragment f = null;
        if (id == GlobalVariables.YOUTUBE_FRAGMENT_ID) {
            f = new YoutubeFragment();
        }
        else if(id == GlobalVariables.INFO_FRAGMENT_ID) {
            f = new InfoFragment();
        }
        else if(id == GlobalVariables.USER_FRAGMENT_ID) {
            f = new UserFragment();
        }
        else if(id == GlobalVariables.USER_EDIT_FRAGMENT_ID) {
            f = new UserEditFragment();
        }
        if(f != null) {
            Bundle args = new Bundle();
            args.putParcelable("user", user);
            f.setArguments(args);
        }
        return f;
    }

    private void nextDayListener() {
        Calendar cal = Calendar.getInstance();
        Intent reset = new Intent(this, NextDay.class);
        AlarmManager alarms ;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, reset, 0);
        alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        alarms.set(AlarmManager.RTC, cal.getTimeInMillis(), alarmIntent);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
