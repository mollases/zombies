package com.mollases.zombies.gmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mollases.zombies.R;

/**
 * A Pin represents a player in the game
 * <p/>
 * Created by mollases on 3/22/14.
 */
public class Pin {

    /**
     * the Pin's associated Marker on the map
     */
    private final MarkerOptions options;
    /**
     * what the server returned for a latitude
     */
    double lat;
    /**
     * what the server returned for longitude
     */
    double lng;
    /**
     * The players current state in the game
     */
    Type type;
    /**
     * the Pin's associated Marker on the map
     */
    private Marker marker;

    /**
     * Initializes a player, along with their corresponding marker position
     *
     * @param type type of pin
     * @param lat  latitude on the gmap
     * @param lng  longitude on the gmap
     */
    public Pin(Type type, double lat, double lng) {
        this.type = type;
        this.lat = lat;
        this.lng = lng;

        options = new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(type.name());

        int resourceId = 0;
        switch (type) {
            case ZOMBIE:
                resourceId = R.drawable.zomb;
                break;
            case HUMAN:
                resourceId = R.drawable.person;
                break;
        }

        if (resourceId != 0) {
            options.icon(BitmapDescriptorFactory.fromResource(resourceId));
        }

        options.title(type.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pin)) return false;

        Pin pin = (Pin) o;

        if (Double.compare(pin.lat, lat) != 0) return false;
        if (Double.compare(pin.lng, lng) != 0) return false;
        return type == pin.type;

    }

    public MarkerOptions getMarker() {
        return options;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLong(double lng) {
        this.lng = lng;
    }


    /**
     * Changes the players icon based on type
     * Note that only Zombies or Humans are drawn
     *
     * @param type the Type to change cast to
     */
    private void updateMarkerType(Type type) {
        int resourceId = 0;
        switch (type) {
            case ZOMBIE:
                resourceId = R.drawable.zomb;
                break;
            case HUMAN:
                resourceId = R.drawable.person;
                break;
        }

        if (resourceId != 0) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(resourceId));
        }

        marker.setTitle(type.name());
    }

    public Type getType() {
        return type;
    }

    public void updateType(Type type) {
        this.type = type;
        updateMarkerType(type);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type.hashCode();
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * An observer is one who cannot play the game, either they have lost, or they have not started.
     *
     * @return whether their type is Observer (lost) or None (not initialized)
     */
    public boolean isObserver() {
        return type == Type.OBSERVER || type == Type.NONE;
    }

    public boolean isHuman() {
        return type == Type.HUMAN;
    }

    public boolean isZombie() {
        return type == Type.ZOMBIE;
    }

    public boolean hasMarker() {
        return marker != null;
    }

    public boolean matches(Type type, double lat, double lng) {
        if (this.type != type) return false;
        if (this.lat != lat) return false;
        if (this.lng != lng) return false;
        return true;
    }

    public void updateTo(Type type, double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        if (type != this.type) {
            updateType(type);
        }

        this.marker.setPosition(new LatLng(this.lat, this.lng));
    }

    public enum Type {
        NONE(0),
        HUMAN(1),
        ZOMBIE(2),
        OBSERVER(3);

        int associatedNumber;

        Type(int associatedNumber) {
            this.associatedNumber = associatedNumber;
        }

        public static Type getTypeByAssociatedNumber(int num) {
            for (Type type : Type.values()) {
                if (type.getAssociatedNumber() == num) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No associated number matches that type");
        }

        public int getAssociatedNumber() {
            return associatedNumber;
        }
    }
}
