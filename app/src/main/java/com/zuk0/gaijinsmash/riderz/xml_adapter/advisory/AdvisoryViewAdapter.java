package com.zuk0.gaijinsmash.riderz.xml_adapter.advisory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.model.bart.Advisory;

import java.util.List;

/**
 * Custom adapters create special layouts for data. Check a corresponding "~list_row.xml" to view
 * the design.
 */

public class AdvisoryViewAdapter extends ArrayAdapter<Advisory> implements View.OnClickListener {

    private Context mContext;

    public AdvisoryViewAdapter(List<Advisory> data, Context context) {
        super(context, R.layout.list_row_station, data);
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView stationName;
        TextView bsaType;
        TextView bsaDescription;
    }

    @Override
    public void onClick(View view) {
    }

    @NonNull
    @Override
    public View getView(int position, View convertView , @NonNull ViewGroup parent) {
        Advisory advisory = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_row_advisory, parent, false);
            viewHolder.stationName = convertView.findViewById(R.id.bsa_station_textView);
            viewHolder.bsaType = convertView.findViewById(R.id.bsa_type_textView);
            viewHolder.bsaDescription = convertView.findViewById(R.id.bsa_description_textView);
            view = convertView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        if (advisory != null) {
            viewHolder.stationName.setText(advisory.getDate());
            viewHolder.bsaType.setText(advisory.getType());
            viewHolder.bsaDescription.setText(advisory.getDescription());
        }
        return view;
    }
}


