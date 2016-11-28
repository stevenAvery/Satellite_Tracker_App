package com.example.savery.satellite_tracker_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class SatelliteRowAdapter extends ArrayAdapter<Satellite> implements Filterable {
    List<Satellite> allSatellites;
    List<Satellite> satellites;

    public SatelliteRowAdapter(Context context, List<Satellite> satellites) {
        super(context, R.layout.satellite_row, satellites);
        this.satellites = satellites;
        this.allSatellites = satellites;
    }

    @Override
    public int getCount() {
        return satellites.size();
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

    // a filter of satellites that meet search query
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults retSatellites = new FilterResults();

                // if there are no constraints on the search query, return everything
                if(constraint == null || constraint.length() == 0) {
                    retSatellites.values = allSatellites;

                // if there are constriants, only return the items that meet them.
                } else {

                    ArrayList<Satellite> filteredSatellites = new ArrayList<>();
                    for(Satellite s : satellites) {
                        // if the item contains the noradId or the name
                        if(s.getNoradId().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            s.getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            filteredSatellites.add(s);
                        }
                    }

                    retSatellites.values = filteredSatellites;
                }

                return retSatellites;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                satellites = (ArrayList<Satellite>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
