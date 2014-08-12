package com.mollases.zombies.ingame;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;
import com.mollases.zombies.gmap.PinSet;
import com.mollases.zombies.gmap.PlayerPin;


/**
 * Created by mollases on 5/8/14.
 */
class ZombLocationListenerClient implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = ZombLocationListenerClient.class.getName();
    private final Context context;
    private final PinSet pins;


    public ZombLocationListenerClient(Context context, PinSet pins) {
        this.context = context;
        this.pins = pins;
    }

    @Override
    public void onLocationChanged(Location l2) {
        PlayerPin playerPin = pins.getPlayerPin(false);

        if (playerPin == null) return;
        playerPin.setLat(l2.getLatitude());
        playerPin.setLong(l2.getLongitude());
        new CreateNewInGamePosition(context).execute(
                String.valueOf(l2.getLatitude()),
                String.valueOf(l2.getLongitude()),
                String.valueOf(playerPin.getType().getAssociatedNumber()));
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "connected");
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
