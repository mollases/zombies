package com.mollases.zombies.gmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by mollases on 3/22/14.
 */
public class Zone {

    private final String name;
    private final int id;


    public Zone(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Zone zone = (Zone) o;

        if (id != zone.id) return false;
        return (name != null ? !name.equals(zone.name) : zone.name != null);


    }

    @Override
    public int hashCode() {
        int result = (name != null ? name.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
