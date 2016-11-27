package com.example.savery.satellite_tracker_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SelectSatelliteActivity extends Activity {
    SatelliteDBHelper dbHelper;
    List<Satellite> satellites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_satellite);

        // setup database
        dbHelper = new SatelliteDBHelper(this);
        dbHelper.deleteAll();
        dbHelper.insert(new Satellite("25544", "ISS (ZARYA)", "stations"));
        dbHelper.insert(new Satellite("19822", "AKEBONO (EXOS-D)"));
        dbHelper.insert(new Satellite("20580", "HST"));
        dbHelper.insert(new Satellite("41304", "SPINSAT"));
        satellites = dbHelper.findAll();

        // add list of satellites to lstSatellites
        ListAdapter satellitesAdapter = new SatelliteRowAdapter(this, satellites);
        ListView lstSatellites = (ListView) findViewById(R.id.lstSatellites);
        lstSatellites.setAdapter(satellitesAdapter);

        // setup click listener for each satellite lstSatellites
        lstSatellites.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    satellitesItemClick(parent, view, position, id);
                }
            }
        );
    }

    // when a satellite item is clicked
    private void satellitesItemClick(AdapterView<?> parent, View view, int position, long id) {
        Satellite satellite = (Satellite)parent.getItemAtPosition(position);

        Toast.makeText(SelectSatelliteActivity.this, satellite.getName(), Toast.LENGTH_LONG).show();

        // Get TLE from celestrak.com
        DownloadTLETask task = new DownloadTLETask(satellite);
        task.execute();

        // show the satellite location on map
        Intent showMapIntent = new Intent(SelectSatelliteActivity.this, SatelliteMap.class);
        showMapIntent.putExtra("name", "Test");
        showMapIntent.putExtra("latitude", 43.6426);
        showMapIntent.putExtra("longitude", -79.3871);

        startActivity(showMapIntent);

    }
}
