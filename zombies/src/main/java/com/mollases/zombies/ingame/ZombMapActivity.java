package com.mollases.zombies.ingame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.mollases.zombies.R;
import com.mollases.zombies.gmap.Pin;
import com.mollases.zombies.gmap.PinSet;
import com.mollases.zombies.postgame.GameStatus;
import com.mollases.zombies.util.DeviceInformation;
import com.mollases.zombies.util.ZTime;

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
public class ZombMapActivity extends Activity {

    private static final Long ONE_SECOND = 1000L;
    private static final int FIVE = 5;
    private static final int THIRTY = 30;
    private static final String TAG = ZombMapActivity.class.getName();
    private final Handler handler = new Handler();
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
        players = new PinSet(this,regId);
        final ZombLocationListenerClient zombLocationListenerClient = new ZombLocationListenerClient(this, players);

        final LocationRequest lr = LocationRequest.create();

        lr.setFastestInterval(FIVE * ONE_SECOND);
        lr.setInterval(THIRTY * ONE_SECOND);
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lc = new LocationClient(this, zombLocationListenerClient, zombLocationListenerClient);

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

        final String tilTime = getIntent().getExtras().getString("until_time");
        final ZTime currentTime = new ZTime();

        final Context that = this;

        Runnable onGameEnd = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(that, GameStatus.class);
                i.putExtra("player_pin_finished_type", players.getPlayerPin().getType());
                i.putExtra("start_time",currentTime.toString());
                i.putExtra("end_time",tilTime);
                startActivity(i);
            }
        };

        handler.postDelayed(onGameEnd, currentTime.timeUntil(tilTime));
    }

    @Override
    public void onResume() {
        lc.connect();
        runAsyncQuery = true;
        new UpdatePlayerPinLocation(this).execute();
        mMapView.onResume();
        super.onResume();
        setUpMapIfNeeded();
        final ZombMapActivity that = this;

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
        lc.disconnect();
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