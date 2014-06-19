package com.mollases.zombies.gmap;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.mollases.zombies.ingame.UpdateInGameUI;

/**
 * Created by mollases on 5/18/14.
 */
public class PlayerPin extends Pin {
    public final static int conversionToZombie = 5;
    public final static int conversionToObserver = 15;
    public final static double distanceForZombie = 0.22;
    public final static double distanceForHuman = 0.35;
    public final static double EARTH_RADIUS_METERS = 6371000.0D;
    private static final String TAG = PlayerPin.class.getName();
    private int warningCount = 0;
    private Activity activity;

    public PlayerPin(Activity activity, Type type, double lat, double lng) {
        super(type, lat, lng);
        this.activity = activity;
        Toast.makeText(activity, type.name(), Toast.LENGTH_SHORT).show();
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        //TODO: find out if this is a valid result.
        Log.i(TAG, "d = " + (c * EARTH_RADIUS_METERS));
        return EARTH_RADIUS_METERS * c;
    }

    public static PlayerPin convertToPlayer(Pin pin, Activity activity) {
        return new PlayerPin(activity, pin.getType(), pin.lat, pin.lng);
    }

    public boolean isTooCloseTo(Pin otherPin) {
        double distance = otherPin.isZombie() ? distanceForZombie : distanceForHuman;
        double delta = getDistanceBetween(otherPin);
        return delta <= distance;
    }

    private double getDistanceBetween(Pin otherPin) {
        return haversine(lat, lng, otherPin.lat, otherPin.lng);
    }

    public void updateType(Type type) {
        Toast.makeText(activity, type.name(), Toast.LENGTH_SHORT).show();
        this.type = type;
    }

    public void calculateNewState(int humanCounter, int zombieCounter) {
        if (isHuman()) {
            updateState(Type.ZOMBIE, zombieCounter - humanCounter, conversionToZombie);
        } else if (isZombie()) {
            updateState(Type.OBSERVER, humanCounter - zombieCounter, conversionToObserver);
        }
    }

    private void updateState(Type newType, int currentOpposingNumber, int conversionNumber) {
        if (currentOpposingNumber <= 0) {
            if (warningCount > 0) {
                warningCount--;
            }
        } else if (warningCount == conversionNumber) {
            if (newType != type) {
                updateType(newType);
            }
            warningCount = 0;
        } else {
            warningCount++;
        }
        new UpdateInGameUI(activity, type, (int)(( (double)warningCount / (double)conversionNumber) * 100)).execute();
    }
}
