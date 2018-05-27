package com.zuk0.gaijinsmash.riderz.xml_adapter.estimate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.fragment.HomeFragment;
import com.zuk0.gaijinsmash.riderz.model.bart.Estimate;
import com.zuk0.gaijinsmash.riderz.model.bart.Trip;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesHelper;

import org.w3c.dom.Text;

import java.util.List;

public class EstimateViewAdapter extends ArrayAdapter<Trip> {

    private HomeFragment mFragment;

    public EstimateViewAdapter(List<Trip> data, Context context, HomeFragment fragment) {
        super(context, R.layout.list_row_estimate, data);
        this.mFragment = fragment;
    }

    private static class ViewHolder {
        TextView origin;
        TextView destination;
        TextView minutes;
        TextView line;
        TextView tag;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Trip trip = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
            convertView = inflater.inflate(R.layout.list_row_estimate, parent, false);
            viewHolder.origin = convertView.findViewById(R.id.etd_originTv);
            viewHolder.destination = convertView.findViewById(R.id.etd_destinationTv);
            viewHolder.minutes = convertView.findViewById(R.id.etd_minutesTv);
            viewHolder.line = convertView.findViewById(R.id.etd_colored_line);
            viewHolder.tag = convertView.findViewById(R.id.etd_minutes_tag_tV);

            view = convertView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        if(trip != null) {
            String origin = trip.getOrigin();
            String destination = trip.getDestination();
            Estimate estimate = trip.getEstimateList().get(0); // only returns the most recent estimate
            String minutes = estimate.getMinutes();
            String color = estimate.getColor();
            viewHolder.origin.setText(origin);
            viewHolder.destination.setText(destination);
            if(minutes.equals("Leaving")) {
                // remove minutes tag
                viewHolder.tag.setText(R.string.now);
            }
            viewHolder.minutes.setText(minutes);
            BartRoutesHelper.setLineBarByColor(mFragment.getActivity(), color, viewHolder.line);
        }
        return view;
    }
}
