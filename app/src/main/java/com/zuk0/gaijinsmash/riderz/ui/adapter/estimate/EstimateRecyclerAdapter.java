package com.zuk0.gaijinsmash.riderz.ui.adapter.estimate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils;

import java.util.List;

public class EstimateRecyclerAdapter extends RecyclerView.Adapter<EstimateRecyclerAdapter.ViewHolder>{

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

    private List<Estimate> mEstimateList;

    public EstimateRecyclerAdapter(List<Estimate> estimateList) { mEstimateList = estimateList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_estimate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estimate estimate = mEstimateList.get(position);
        holder.origin.setText(estimate.getOrigin());
        holder.destination.setText(estimate.getDestination());

        String minutes = estimate.getMinutes();
        holder.minutes.setText(minutes);
        if(minutes.equals("Leaving")) {
            holder.tag.setText(R.string.now);
        }
        BartRoutesUtils.setLineBarByColor(holder.line.getContext(), estimate.getColor(), holder.line);
    }

    @Override
    public int getItemCount() {
        return mEstimateList.size();
    }
}
