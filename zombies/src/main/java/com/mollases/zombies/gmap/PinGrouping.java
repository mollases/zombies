package com.mollases.zombies.gmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mollases on 3/22/14.
 */
class PinGrouping {
    private final
    Set<Pin> pinset = new HashSet<Pin>();
    private final Pin.Type type;

    public PinGrouping(Pin.Type type, Pin... pins) {
        pinset.addAll(Arrays.asList(pins));
        this.type = type;
    }

    public MarkerOptions getMarker() {
        return new MarkerOptions()
                .position(new LatLng(44.854288, -93.242147))
                .title(type.name())
                .snippet("Day of destruction");
    }

    public void clear() {
        pinset.clear();
    }
}
