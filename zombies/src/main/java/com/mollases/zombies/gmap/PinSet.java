package com.mollases.zombies.gmap;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

/**
 * The Grouping of Pins.
 */
public class PinSet {
    private final Map<Integer, Pin> players = new HashMap<Integer, Pin>();
    private final Activity activity;
    private boolean changed = false;
    private int followId;
    private PlayerPin playerPin;

    public PinSet(Activity activity, int followId) {
        this.followId = followId;
        this.activity = activity;
    }

    public boolean changed() {
        boolean retVal = changed;
        changed = false;
        return retVal;
    }

    public void updateMap(GoogleMap mMap) {
        for (Map.Entry<Integer, Pin> entry : players.entrySet()) {
            if (entry.getKey().equals(followId)) {
                continue;
            }
            Pin player = entry.getValue();
            if (!player.isObserver()) {
                if (!player.hasMarker()) {
                    Marker m = mMap.addMarker(player.getMarker());
                    player.setMarker(m);
                }
            }
        }
    }

    public PlayerPin getPlayerPin(boolean watchForNull) {
        if (!watchForNull || playerPin != null) {
            return playerPin;
        }

        playerPin = createPlayerPin();
        return playerPin;
    }


    private PlayerPin createPlayerPin() {
        Pin pin = players.get(followId);

        if (pin != null) {
            return PlayerPin.convertToPlayer(pin, activity);
        }
        throw new IllegalArgumentException("No player pin defined");
    }

    public PlayerPin getPlayerPin() {
        return getPlayerPin(true);
    }

    public void checkLocationAgainstPinSet() {
        PlayerPin playerPin = getPlayerPin();

        int humanCounter = 0;
        int zombieCounter = 0;
        if (playerPin.isObserver()) {
            return;
        }

        for (Map.Entry<Integer, Pin> entry : players.entrySet()) {
            Pin pin = entry.getValue();
            if (entry.getKey().equals(followId)) {
                continue;
            }
            if (!pin.isObserver()) {
                if (playerPin.isTooCloseTo(pin)) {
                    if (pin.isHuman()) {
                        humanCounter++;
                    } else if (pin.isZombie()) {
                        zombieCounter++;
                    }
                }
            }
        }

        playerPin.calculateNewState(humanCounter, zombieCounter);
    }

    public void updatePin(int id, Pin.Type type, double lat, double lng) {
        Pin gottenPin = players.get(id);
        if (gottenPin == null) {
            changed = true;
            players.put(id, new Pin(type, lat, lng));
        } else {
            if (!gottenPin.matches(type, lat, lng)) {
                changed = true;
                gottenPin.updateTo(type, lat, lng);
            }
        }
    }
}
