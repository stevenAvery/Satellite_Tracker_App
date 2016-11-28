package com.example.savery.satellite_tracker_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SatelliteMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude, longitude;
    private double futureLatitude, futureLongitude;
    private String name;

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
        latitude = callingIntent.getDoubleExtra("latitude", 0.0);
        longitude = callingIntent.getDoubleExtra("longitude", 0.0);
        futureLatitude = callingIntent.getDoubleExtra("futureLatitude", 0.0);
        futureLongitude = callingIntent.getDoubleExtra("futureLongitude", 0.0);
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
        LatLng satelliteLatLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(satelliteLatLng).title(name));
        PolylineOptions orbit = new PolylineOptions()
            .add(satelliteLatLng)
            .add(new LatLng(futureLatitude, futureLongitude))
            .width(10.0f)
            .color(Color.YELLOW)
            .geodesic(true);
        mMap.addPolyline(orbit);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(satelliteLatLng));
    }
}
