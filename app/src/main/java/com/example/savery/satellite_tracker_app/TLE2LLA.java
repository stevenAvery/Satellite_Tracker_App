package com.example.savery.satellite_tracker_app;

import android.util.Pair;

import com.example.savery.satellite_tracker_app.sgp.Satelite;
import com.example.savery.satellite_tracker_app.sgp.TLE;
import com.example.savery.satellite_tracker_app.sgp.Tiempo;

public class TLE2LLA {
    public static Pair<Double, Double> getLLA(String[] inTle, double offset) {
        double lat, lng;

        com.example.savery.satellite_tracker_app.sgp.TLE tle = new TLE(inTle[0], inTle[1], inTle[2]);
        Satelite sat = new Satelite(tle);

        // calculate the vel line
        sat.calcularVariables(Tiempo.getCurrentUniversalJulianTime(offset));
        lat = sat.latitud;
        lng = sat.longitud;

        return new Pair<>(lat, lng);
    }
}
