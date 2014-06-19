package com.mollases.zombies.pregame.locationanalyzer;

import android.content.Context;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.mollases.zombies.gmap.Zone;


/**
 * Created by mollases on 5/8/14.
 */
class ZombLocationAnalyzerListenerClient implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = ZombLocationAnalyzerListenerClient.class.getName();
    private final Context context;
    private final GoogleMap mMap;
    private Zone zone;
    private int mapperId;
    private boolean recorderEnabled;


    public ZombLocationAnalyzerListenerClient(Context context, GoogleMap mMap) {
        this.context = context;
        this.mMap = mMap;
    }

    public void setMapperId(int mapperId) {
        this.mapperId = mapperId;
    }

    public void setRecorderEnabled(boolean enabled) {
        recorderEnabled = enabled;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
        this.zone.setShowing(false);
    }

    @Override
    public void onLocationChanged(Location l2) {
        if (zone != null && !zone.isShowing()) {
            mMap.addPolygon(zone.setPolygonFromPoints());
        }
        String toastString = "location updated";
        // mMap.animateCamera(cameraUpdate);
        if (recorderEnabled) {
            toastString += ", recorder enabled";
            if (mapperId > 0) {
                toastString += ", location sent";
                new UpdateMapperLocationData(context).execute(
                        String.valueOf(mapperId),
                        String.valueOf(l2.getLatitude()),
                        String.valueOf(l2.getLongitude()),
                        String.valueOf(l2.getAltitude()));
            } else {
                toastString += ", mo map id";
            }
        }
        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
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
