package com.example.savery.satellite_tracker_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectSatelliteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_satellite);

        // get satellites from database
        Satellite[] satellites = {
            new Satellite("ISS (ZARYA)", "25544"),
            new Satellite("AKEBONO (EXOS-D)", "19822"),
            new Satellite("HST", "20580"),
        };
        //String[] satellites = {"ISS (ZARYA)", "AKEBONO (EXOS-D)", "HST"};

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
        String satellite = String.valueOf(parent.getItemAtPosition(position));
        Toast.makeText(SelectSatelliteActivity.this, satellite, Toast.LENGTH_LONG).show();
    }
}
