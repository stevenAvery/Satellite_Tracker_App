package com.example.savery.satellite_tracker_app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.savery.satellite_tracker_app.sgp.Satelite;
import com.example.savery.satellite_tracker_app.sgp.TLE;
import com.example.savery.satellite_tracker_app.sgp.Tiempo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SelectSatelliteActivity extends Activity {
    SatelliteDBHelper dbHelper;
    List<Satellite> satellites;
    private SatelliteRowAdapter satellitesAdapter;
    private ListView lstSatellites;
    private EditText txtSearchSatellites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_satellite);

        // setup database
        dbHelper = new SatelliteDBHelper(this);
        satellites = dbHelper.findAll();

        // add list of satellites to lstSatellites
        satellitesAdapter = new SatelliteRowAdapter(this, satellites);
        lstSatellites = (ListView) findViewById(R.id.lstSatellites);
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

        // add search functionality to txtSearchSatellites
        txtSearchSatellites = (EditText) findViewById(R.id.txtSearchSatellites);
        txtSearchSatellites.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SelectSatelliteActivity.this.satellitesAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // when a satellite item is clicked
    private void satellitesItemClick(AdapterView<?> parent, View view, int position, long id) {
        Satellite satellite = (Satellite) parent.getItemAtPosition(position);

        Toast.makeText(SelectSatelliteActivity.this, "Loading " + satellite.getName() + " data.", Toast.LENGTH_LONG).show();

        // Get TLE from celestrak.com
        DownloadTLETask task = new DownloadTLETask(satellite);
        task.execute();
    }


    // Asynchronously gets data from given URL
    class DownloadTLETask extends AsyncTask<String, Void, String[]> {
        private String TLE = null;
        private Exception exception = null;
        private String URLString = "";

        String baseURL = "https://www.space-track.org";
        String authPath = "/auth/login";
        String logoutPath = "/ajaxauth/logout";
        String userName = "steven.avery@uoit.net";
        String password = "PanhbJOASg6Xl994";
        String query = "";

        public DownloadTLETask(Satellite satellite) {
            query = "/basicspacedata/query/class/tle_latest/ORDINAL/1/NORAD_CAT_ID/" +
                    satellite.getNoradId() +
                    "/orderby/TLE_LINE1%20ASC/format/tles";
        }

        @Override
        protected String[] doInBackground(String... params) {
            String tle0 = null,
                    tle1 = null,
                    tle2 = null;
            try {
                CookieManager manager = new CookieManager();
                manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                CookieHandler.setDefault(manager);

                // login
                URL url = new URL(baseURL + authPath);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                String input = "identity=" + userName + "&password=" + password;

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String output;
                Log.v("TLE", "Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    Log.v("TLE", output);
                }

                // get tle information
                url = new URL(baseURL + query);
                br = new BufferedReader(new InputStreamReader((url.openStream())));

                if ((output = br.readLine()) != null) {
                    Log.d("TLE", output);
                    // extract the TLEs from the acquired JSON
                    JSONArray jsonOutputArray = new JSONArray(output);
                    JSONObject jsonOutput = jsonOutputArray.getJSONObject(0); // get index 0
                    tle0 = jsonOutput.getString("TLE_LINE0");
                    tle1 = jsonOutput.getString("TLE_LINE1");
                    tle2 = jsonOutput.getString("TLE_LINE2");
                }

                // logout
                url = new URL(baseURL + logoutPath);
                br = new BufferedReader(new InputStreamReader((url.openStream())));
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                String[] retArray = {tle0, tle1, tle2};
                return retArray;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            // handle any error
            if (exception != null) {
                exception.printStackTrace();
                return;
            }

            Pair<Double, Double> pos0 = TLE2LLA.getLLA(result, 0.0);

            // show the satellite location on map
            Intent showMapIntent = new Intent(SelectSatelliteActivity.this, SatelliteMap.class);
            showMapIntent.putExtra("name", result[0].substring(2));
            showMapIntent.putExtra("tle", result);
            showMapIntent.putExtra("latitude", pos0.first);
            showMapIntent.putExtra("longitude", pos0.second);

            startActivity(showMapIntent);
        }
    }
}