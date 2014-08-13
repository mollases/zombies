package com.mollases.zombies.ingame;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.mollases.zombies.R;
import com.mollases.zombies.gmap.Pin;
import com.mollases.zombies.gmap.PinSet;
import com.mollases.zombies.util.DeviceInformation;

import java.util.ArrayList;

/**
 * Created by mollases on 4/11/14.
 * <p/>
 * CAUTION: Battery Intensive!
 * Map Location Analyzer:
 * gives the user the option to start fielding a location
 * the user can swipe from the side to draw the menu
 * the user can choose to create a new map region or use an old one
 * if the user creates a new one, they give it a title
 * and the querier begins recording data
 * until they stop the activity or stop recording
 */
public class ZombMapFragment extends Activity {

    private static final Long ONE_SECOND = 1000L;
    private static final int FIVE = 5;
    private static final int THIRTY = 30;
    private static final String TAG = ZombMapFragment.class.getName();
    private final Handler handler = new Handler();
    private ActionBarDrawerToggle mDrawerToggle;
    private LocationClient lc;
    private MapView mMapView;
    private GoogleMap mMap;
    private PinSet players;
    private int regId;
    private boolean runAsyncQuery;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zomb_map);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        setUpMapIfNeeded();


        regId = DeviceInformation.getRegistrationId(this);
        players = new PinSet(this, regId);
        final ZombLocationListenerClient zombLocationListenerClient = new ZombLocationListenerClient(this, players);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.side_menu_drawer);
        ListView mDrawerList = (ListView) findViewById(R.id.side_menu_list);

        ArrayList<String> navDrawerItems = new ArrayList<String>();
        navDrawerItems.add("test");
        ArrayAdapter<String> mDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);


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
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerClosed(View view) {
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        final LocationRequest lr = LocationRequest.create();

        lr.setFastestInterval(FIVE * ONE_SECOND);
        lr.setInterval(THIRTY * ONE_SECOND);
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lc = new LocationClient(this, zombLocationListenerClient, zombLocationListenerClient);

        lc.connect();
        lc.registerConnectionCallbacks(new GooglePlayServicesClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                lc.requestLocationUpdates(lr, zombLocationListenerClient);
            }

            @Override
            public void onDisconnected() {
                lc.removeLocationUpdates(zombLocationListenerClient);
            }
        });
    }

    @Override
    public void onResume() {
        runAsyncQuery = true;
        new UpdatePlayerPinLocation(this).execute();
        mMapView.onResume();
        super.onResume();
        setUpMapIfNeeded();
        mDrawerToggle.syncState();
        final ZombMapFragment that = this;

        Runnable queryServer = new Runnable() {
            @Override
            public void run() {
                if(runAsyncQuery)
                handler.postDelayed(this, 5000L);
                new GetLatestInGamePositions(that).execute();
            }
        };

        handler.postDelayed(queryServer, 0L);
    }

    @Override
    public void onPause() {
        runAsyncQuery = false;
        mMapView.onPause();
        super.onPause();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapView) findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        MapsInitializer.initialize(this);
    }

    @Override
    protected void onDestroy() {
        lc.disconnect();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
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

    public void scanMap() {
        if (players.changed()) {
            players.updateMap(mMap);
        }
        players.checkLocationAgainstPinSet();
    }

    public void setPlayerPin(Pin.Type type, double lat, double lng) {
        players.updatePin(regId, type, lat, lng);
        players.getPlayerPin().calculateNewState(0, 0);
    }

    public void updatePin(int id, Pin.Type type, double lat, double lng) {
        players.updatePin(id, type, lat, lng);
    }
}