package com.example.gaijinsmash.transitapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Station;

import java.util.List;

/**
 * Created by ryanj on 7/31/2017.
 */

public class StationCustomAdapter extends ArrayAdapter<Station> implements View.OnClickListener {

    private List<Station> stationList = null;
    Context mContext;

    public StationCustomAdapter(List<Station> data, Context context) {
        super(context, R.layout.station_list_row, data);
        this.stationList = data;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView stationName;
        TextView stationAdrress;
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Object object = getItem(position);
        Station station = (Station) object;

        switch (view.getId()) {
            // do something
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Station station = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.station_list_row, parent, false);
            viewHolder.stationName = (TextView) convertView.findViewById(R.id.stationName_textView);
            viewHolder.stationAdrress = (TextView) convertView.findViewById(R.id.stationAddress_textView);
            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        viewHolder.stationName.setText(station.getName());

        return convertView;
    }
}
