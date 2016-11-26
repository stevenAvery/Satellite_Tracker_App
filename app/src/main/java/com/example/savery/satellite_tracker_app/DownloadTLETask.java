package com.example.savery.satellite_tracker_app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// Asynchronously gets data from given URL
class DownloadTLETask extends AsyncTask<String, Void, String> {
    private String TLE = null;
    private Exception exception = null;
    private String URLString = "https://celestrak.com/NORAD/elements/";
    private Satellite satellite;

    public DownloadTLETask(Satellite satellite) {
        this.satellite = satellite;
        URLString += satellite.getType() + ".txt";
        //URLString = "https://celestrak.com/NORAD/elements/stations.txt";
        URLString = "https://blockchain.info/tobtc?currency=CAD&value=49.99";
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("TLE", URLString);
            Log.d("TLE", "TESTING -------------------- 1");

            String line = null;
            URL url = new URL(URLString);
            HttpURLConnection conn;
            Log.d("TLE", "TESTING -------------------- 1.1");
            conn = (HttpURLConnection)url.openConnection();
            Log.d("TLE", "TESTING -------------------- 1.2");
            int result = conn.getResponseCode();
            Log.d("TLE", "TESTING -------------------- 2");
            if (result == HttpURLConnection.HTTP_OK) {
                Log.d("TLE", "TESTING -------------------- 3");

                InputStream inStream = conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

                if ((line = in.readLine()) == null)
                    Log.v("TLE: ", "Couldn't get any information from celestrak.com");

                do {
                    Log.v("TLE", "Line: " + line);
                    if(line.equals(satellite.getName())) {
                        Log.v("TLE: ", "Found " + line);
                    }
                } while((line = in.readLine()) != null);

                in.close();
            }
        } catch (Exception  e) {
            e.printStackTrace();
        } finally {
            //Log.v("TLE: ", ""+TLE);
            return TLE;
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
}