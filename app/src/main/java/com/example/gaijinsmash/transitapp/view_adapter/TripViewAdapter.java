package com.example.gaijinsmash.transitapp.view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Trip;

import java.util.List;

public class TripViewAdapter  extends ArrayAdapter<Trip> implements View.OnClickListener {
    private List<Trip> mTripList = null;
    private Context mContext;

    public TripViewAdapter(List<Trip> data, Context context) {
        super(context, R.layout.trip_list_row, data);
        this.mTripList = data;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView origin;
        TextView destination;
        TextView fare;
    }

    @Override
    public void onClick(View view) {
        int position  = (Integer) view.getTag();
        Object object = getItem(position);
        Trip trip = (Trip) object;

        switch (view.getId()) {
            // TODO: do something on click?
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        Trip trip = getItem(position);
        TripViewAdapter.ViewHolder viewHolder;
        final View view;
        if(convertView == null) {
            viewHolder = new TripViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.trip_list_row, parent, false);
            viewHolder.origin = (TextView) convertView.findViewById(R.id.trip_origin_textView);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.trip_destination_textView);
            viewHolder.fare = (TextView) convertView.findViewById(R.id.trip_fare_textView);
            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TripViewAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        viewHolder.origin.setText(trip.getOrigin());
        viewHolder.destination.setText(trip.getDestination());
        viewHolder.fare.setText(trip.getFare());
        return convertView;
    }
}
