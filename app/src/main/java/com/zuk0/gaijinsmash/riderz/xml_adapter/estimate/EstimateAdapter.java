package com.zuk0.gaijinsmash.riderz.xml_adapter.estimate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.model.bart.Estimate;
import com.zuk0.gaijinsmash.riderz.model.bart.Trip;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesHelper;

import java.util.List;

public class EstimateAdapter extends RecyclerView.Adapter<EstimateAdapter.ViewHolder>{

    private List<Trip> mTripList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView origin;
        TextView destination;
        TextView minutes;
        TextView line;
        TextView tag;

        ViewHolder(View view) {
            super(view);
            origin = view.findViewById(R.id.etd_originTv);
            destination = view.findViewById(R.id.etd_destinationTv);
            minutes = view.findViewById(R.id.etd_minutesTv);
            line = view.findViewById(R.id.etd_colored_line);
            tag = view.findViewById(R.id.etd_minutes_tag_tV);
        }
    }

    public EstimateAdapter(List<Trip> tripList) { mTripList = tripList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_estimate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTripList.get(position);

        holder.origin.setText(trip.getOrigin());
        holder.destination.setText(trip.getDestination());

        Estimate estimate = trip.getEstimateList().get(0); // only returns the most recent estimate
        String minutes = estimate.getMinutes();
        holder.minutes.setText(minutes);
        if(minutes.equals("Leaving")) {
            holder.tag.setText(R.string.now);
        }
        holder.minutes.setText(minutes);
        BartRoutesHelper.setLineBarByColor(holder.line.getContext(), estimate.getColor(), holder.line);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
