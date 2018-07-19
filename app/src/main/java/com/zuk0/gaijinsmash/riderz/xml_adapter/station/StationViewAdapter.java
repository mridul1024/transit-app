package com.zuk0.gaijinsmash.riderz.xml_adapter.station;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.model.Station;

import java.util.List;

/**
 * Custom adapters create special layouts for data. Check a corresponding "~list_row.xml" to view
 * the design.
 */

public class StationViewAdapter extends ArrayAdapter<Station> implements View.OnClickListener {

    private Context mContext;

    public StationViewAdapter(List<Station> data, Context context) {
        super(context, R.layout.list_row_station, data);
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView stationName;
        TextView stationAddress;
        TextView stationCity;
    }

    @Override
    public void onClick(View view) {
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Station station = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_row_station, parent, false);
            viewHolder.stationName = convertView.findViewById(R.id.stationName_textView);
            viewHolder.stationAddress = convertView.findViewById(R.id.stationAddress_textView);
            viewHolder.stationCity = convertView.findViewById(R.id.stationCity_textView);
            view = convertView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        String name;
        String newName;
        if (station != null) {
            name = station.getName();
            newName = name.replaceAll("International", "Int'l");
            viewHolder.stationName.setText(newName);
            viewHolder.stationAddress.setText(station.getAddress());
            viewHolder.stationCity.setText(station.getCity());
        }
        return view;
    }
}
