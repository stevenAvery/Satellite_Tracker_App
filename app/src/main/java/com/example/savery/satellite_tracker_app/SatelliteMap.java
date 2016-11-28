package com.example.savery.satellite_tracker_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SatelliteMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String name;
    private double lat0, lng0;
    private String[] tle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get information from intent call
        Intent callingIntent = getIntent();
        name = callingIntent.getStringExtra("name");
        tle = callingIntent.getStringArrayExtra("tle");
        lat0 = callingIntent.getDoubleExtra("latitude", 0.0);
        lng0 = callingIntent.getDoubleExtra("longitude", 0.0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the satellite location and move the camera
        LatLng satelliteLatLng = new LatLng(lat0, lng0);
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(satelliteLatLng).title(name));
        Polyline orbit = mMap.addPolyline(new PolylineOptions()
            .width(10.0f)
            .color(Color.BLUE)
            .geodesic(true));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(satelliteLatLng));

        animateMarker(mMarker, orbit);
    }

    // automatically updates the marker on the map
    public void animateMarker(final Marker marker, final Polyline orbit) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;

                Pair<Double, Double> newPos = TLE2LLA.getLLA(tle, 0.0);
                Pair<Double, Double> newPos1 = TLE2LLA.getLLA(tle, 1.0);
                List<LatLng> points = new ArrayList<>(Arrays.asList(
                    new LatLng(newPos.first, newPos.second),
                    new LatLng(newPos1.first, newPos1.second)
                ));

                marker.setPosition(new LatLng(newPos.first, newPos.second));
                orbit.setPoints(points);

                // Post again 2000ms later.
                handler.postDelayed(this, 2000);
            }
        });
    }
}
