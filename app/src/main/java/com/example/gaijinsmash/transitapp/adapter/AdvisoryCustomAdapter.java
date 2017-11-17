package com.example.gaijinsmash.transitapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Advisory;

/**
 * Custom adapters create special layouts for data. Check a corresponding "~list_row.xml" to view
 * the design.
 */

import java.util.List;

public class AdvisoryCustomAdapter extends ArrayAdapter<Advisory> implements View.OnClickListener {
    private List<Advisory> mAdvisoryList = null;
    private Context mContext;

    public AdvisoryCustomAdapter(List<Advisory> data, Context context) {
        super(context, R.layout.station_list_row, data);
        this.mAdvisoryList = data;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView stationName;
        TextView bsaType;
        TextView bsaDescription;
    }

    @Override
    public void onClick(View view) {
        int position  = (Integer) view.getTag();
        Object object = getItem(position);
        Advisory advisory = (Advisory) object;

        switch (view.getId()) {
            // TODO: do something on click?
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        Advisory advisory = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.advisory_list_row, parent, false);
            viewHolder.stationName = (TextView) convertView.findViewById(R.id.bsa_station_textView);
            viewHolder.bsaType = (TextView) convertView.findViewById(R.id.bsa_type_textView);
            viewHolder.bsaDescription = (TextView) convertView.findViewById(R.id.bsa_description_textView);
            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        viewHolder.stationName.setText(advisory.getDate());
        viewHolder.bsaType.setText(advisory.getType());
        viewHolder.bsaDescription.setText(advisory.getDescription());
        return convertView;
    }
}


