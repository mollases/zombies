package com.mollases.zombies.pregame;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mollases.zombies.R;
import com.mollases.zombies.pregame.joinablegames.JoinableGamesFragment;
import com.mollases.zombies.pregame.joinablegames.activegames.ActiveGamesFragment;
import com.mollases.zombies.pregame.locationanalyzer.ZombMapLocationAnalyzer;
import com.mollases.zombies.pregame.newgame.GameSelectorFragment;
import com.mollases.zombies.pregame.sidemenu.MenuOption;
import com.mollases.zombies.pregame.sidemenu.MenuOptionArrayAdapter;
import com.mollases.zombies.util.DeviceInformation;

import java.util.ArrayList;


public class PreGameActivities extends Activity implements GameSelectorFragment.OnLocationSaved {
    private static final String TAG = PreGameActivities.class.getName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;

    // For GameSelectorFragment;
    private String gsfTitle;
    private double gsfLat;
    private double gsfLong;
    private double gsfDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int registrationId = DeviceInformation.getRegistrationId(this);
        if (registrationId == DeviceInformation.DEFAULT_REG_ID) {
            new StoreNewPhoneId(this).execute();
        }

        setContentView(R.layout.activity_pre_game_activities);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.side_menu_drawer);
        mDrawerList = (ListView) findViewById(R.id.side_menu_list);

        ArrayList<MenuOption> navDrawerItems = new ArrayList<MenuOption>();
        navDrawerItems.add(MenuOption.GAME_CREATOR);
        navDrawerItems.add(MenuOption.GAME_AVAIL_VIEWER);
        navDrawerItems.add(MenuOption.GAME_ACTIVE_VIEWER);
        navDrawerItems.add(MenuOption.MAP_ANALYSIS_TOOL);

        mDrawerList.setAdapter(new MenuOptionArrayAdapter(this, R.layout.menu_option, navDrawerItems));

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.abc_ab_bottom_solid_dark_holo, //nav menu toggle icon
                R.string.app_name,
                // nav drawer open - description for accessibility
                R.string.app_name
                // nav drawer close - description for accessibility
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // set up watchers
        if(getIntent().getExtras() != null){
            gsfTitle = getIntent().getExtras().getString("title");
            gsfLat = getIntent().getExtras().getDouble("lat");
            gsfLong = getIntent().getExtras().getDouble("long");
            gsfDelta = getIntent().getExtras().getDouble("delta");
        }

        if(gsfDelta != 0){
            displayView(MenuOption.GAME_CREATOR,false);
        } else if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(MenuOption.GAME_ACTIVE_VIEWER, false);
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during onPostCreate() and
     * onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        return mDrawerToggle.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    private void displayView(MenuOption option) {
        displayView(option, true);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(MenuOption option, boolean addToStack) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        setTitle(option.getOptionDesc());
        switch (option) {
            case GAME_CREATOR: {
                fragment = new GameSelectorFragment();
                break;
            }
            case GAME_AVAIL_VIEWER: {
                fragment = new JoinableGamesFragment();
                break;
            }
            case GAME_ACTIVE_VIEWER: {
                fragment = new ActiveGamesFragment();
                break;
            }
            case MAP_ANALYSIS_TOOL: {
                Intent i = new Intent(this, ZombMapLocationAnalyzer.class);
                startActivity(i);
                break;
            }
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (addToStack) {
                fragmentTransaction.addToBackStack(option.name());
            }
            fragmentTransaction.replace(R.id.side_menu_frame, fragment);
            fragmentTransaction.commit();

            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    @Override
    public String locationSavedTitle() {
        return gsfTitle;
    }

    @Override
    public double locationSavedLatitude() {
        return gsfLat;
    }

    @Override
    public double locationSavedLongitude() {
        return gsfLong;
    }

    @Override
    public double locationSavedDelta() {
        return gsfDelta;
    }


    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView((MenuOption) parent.getItemAtPosition(position));
        }
    }
}