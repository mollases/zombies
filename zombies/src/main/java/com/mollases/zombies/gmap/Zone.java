package com.mollases.zombies.gmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

/**
 * Created by mollases on 3/22/14.
 */
public class Zone {

    private final Type type;
    private final String name;
    private final int id;
    private final List<LatLng> points;
    private boolean showing = false;

    public Zone(Type type, String name, int id, List<LatLng> points) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.points = points;
    }

    public PolygonOptions setPolygonFromPoints() {
        showing = true;
        return new PolygonOptions()
                .add(points.toArray(new LatLng[points.size()]))
                .strokeColor(type.strokeColor)
                .fillColor(type.fillColor)
                .strokeWidth(type.strokeSize);
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Zone zone = (Zone) o;

        if (id != zone.id) return false;
        if (name != null ? !name.equals(zone.name) : zone.name != null) return false;
        if (points != null ? !points.equals(zone.points) : zone.points != null) return false;
        return type == zone.type;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }

    public enum Type {
        SAFEPLACE(1),
        UNACTIVE(0x88def1ff, 0xcc237d82, 5),
        STORE(1),
        HOSPITAL(0x88def1ff, 0xcc237d82, 5),
        MATCHBOUNDS(1);

        final int fillColor;
        final int strokeColor;
        final int strokeSize;


        Type(int fillColor) {
            this(fillColor, 0xffffffff, 10);
        }


        Type(int fillColor, int strokeColor, int strokeSize) {
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.strokeSize = strokeSize;
        }
    }
}
