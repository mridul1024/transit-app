package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.ViewHolder>{

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private TextView city;
        private TextView abbr;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.stationName_textView);
            address = view.findViewById(R.id.stationAddress_textView);
            city = view.findViewById(R.id.stationCity_textView);
            abbr = view.findViewById(R.id.stationAbbr_textView);
        }
    }

    private View.OnClickListener mClickListener;
    private List<Station> mStationList;

    public StationRecyclerAdapter(List<Station> stationList) { mStationList = stationList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_station, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> mClickListener.onClick(v));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Station station = mStationList.get(i);
        String name = station.getName();
        String modifiedName = name.replaceAll("International", "Int'l") + " " + viewHolder.name.getContext().getString(R.string.station);
        viewHolder.name.setText(modifiedName);
        viewHolder.address.setText(station.getAddress());
        viewHolder.city.setText(station.getCity());
        viewHolder.abbr.setText(station.getAbbr());
    }

    @Override
    public int getItemCount() {
        return mStationList.size();
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

}
