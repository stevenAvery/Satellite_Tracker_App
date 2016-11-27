package com.example.savery.satellite_tracker_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


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


// Asynchronously gets data from given URL
class DownloadTLETask extends AsyncTask<String, Void, String> {
    private String TLE = null;
    private Exception exception = null;
    private String URLString = "";

    String baseURL    = "https://www.space-track.org";
    String authPath   = "/auth/login";
    String logoutPath = "/ajaxauth/logout";
    String userName   = "steven.avery@uoit.net";
    String password   = "PanhbJOASg6Xl994";
    String query      = "";

    public DownloadTLETask(Satellite satellite) {
        query = "/basicspacedata/query/class/tle_latest/ORDINAL/1/NORAD_CAT_ID/" +
                satellite.getNoradId() +
                "/orderby/TLE_LINE1%20ASC/format/tles";
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);

            // login
            URL url = new URL(baseURL+authPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            String input = "identity="+userName+"&password="+password;

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
            url = new URL(baseURL+query);
            br = new BufferedReader(new InputStreamReader((url.openStream())));

            //while ((output = br.readLine()) != null) {
            //    Log.v("TLE", output);
            //}

            if ((output = br.readLine()) != null) {
                // extract the TLEs from the acquired JSON
                JSONArray jsonOutputArray = new JSONArray(output);
                JSONObject jsonOutput = jsonOutputArray.getJSONObject(0); // get index 0
                String tle1 = jsonOutput.getString("TLE_LINE1");
                String tle2 = jsonOutput.getString("TLE_LINE2");

                TLEtoLLA(tle1, tle2);
            }

            // logout
            url = new URL(baseURL + logoutPath);
            br = new BufferedReader(new InputStreamReader((url.openStream())));
            conn.disconnect();
        } catch (Exception  e) {
            e.printStackTrace();
        } finally {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // handle any error
        if (exception != null) {
            exception.printStackTrace();
            return;
        }

        //Log.v("TLE: ", ""+TLE);
    }

    private void TLEtoLLA(String tle1, String tle2) {
        Log.d("TLE1", tle1);
        Log.d("TLE2", tle2);
    }
}