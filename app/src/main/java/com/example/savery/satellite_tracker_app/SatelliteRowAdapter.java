package com.example.savery.satellite_tracker_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class SatelliteRowAdapter extends ArrayAdapter<Satellite> {

    public SatelliteRowAdapter(Context context, Satellite[] satellites) {
        super(context, R.layout.satellite_row, satellites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater satelliteInflater = LayoutInflater.from(getContext());
        View satelliteRowView = satelliteInflater.inflate(R.layout.satellite_row, parent, false);

        Satellite satellite = getItem(position);
        TextView name = (TextView) satelliteRowView.findViewById(R.id.lblSatelliteName);
        TextView noradId = (TextView) satelliteRowView.findViewById(R.id.lblNoradId);

        name.setText(satellite.getName());
        noradId.setText(satellite.getNoradId());

        return satelliteRowView;
    }
}
