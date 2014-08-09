package com.mollases.zombies.pregame.locationanalyzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mollases.zombies.R;
import com.mollases.zombies.gmap.Zone;

import java.util.Set;

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
public class ZombMapLocationAnalyzer extends Activity {
    private String TAG = ZombMapLocationAnalyzer.class.getName();

    private double DEFAULT_DELTA = 200D; // 200 meters

    private MapView mMapView;
    private GoogleMap mMap;
    private Marker marker;
    private Circle circle;
    private double delta;

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double r = 6731000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return c * r;
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_analyzer);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        setUpMapIfNeeded();

    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
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

    private void resetMap() {

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        marker = null;
        circle = null;
        mMap.clear();
    }

    private void setUpMap() {
        resetMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                resetMap();
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.getUiSettings().setAllGesturesEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                circle = mMap.addCircle(new CircleOptions().center(latLng).radius(setToDefaultDelta()));
                marker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng));
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker2) {
                delta = haversine(circle.getCenter().latitude
                        , circle.getCenter().longitude
                        , marker2.getPosition().latitude
                        , marker2.getPosition().longitude
                );
                circle.setRadius(delta);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        MapsInitializer.initialize(this);
    }

    private double setToDefaultDelta(){
        delta = DEFAULT_DELTA;
        return delta;
    }

    @Override
    protected void onDestroy() {

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

    public void onSavePressed(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.lctn_nlzr_save_marker);

        final View v = View.inflate(this, R.layout.view_store_ring_detail, null);
        ((TextView) v.findViewById(R.id.view_store_ring_detail_ring_size_value)).setText(String.valueOf(String.valueOf(delta)));

        alert.setView(v);

        alert.setPositiveButton(R.string.view_store_ring_detail_verify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = ((TextView) v.findViewById(R.id.view_store_ring_detail_title)).getText().toString();
            }
        });

        alert.show();
    }
}