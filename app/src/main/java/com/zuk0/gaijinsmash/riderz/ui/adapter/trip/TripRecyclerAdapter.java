package com.zuk0.gaijinsmash.riderz.ui.adapter.trip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trip_origin_textView) TextView origin1;
        @BindView(R.id.trip_destination_textView) TextView destination1;
        @BindView(R.id.trip_departTime_textView) TextView origTimeMin1;
        @BindView(R.id.trip_arrivalTime_textView) TextView destTimeMin1;

        @BindView(R.id.results_leg2) RelativeLayout leg2Layout; //layout container
        @BindView(R.id.results_leg3) RelativeLayout leg3Layout; //layout container

        @BindView(R.id.trip_transferLayout1) LinearLayout transferLayout1; //icon
        @BindView(R.id.trip_transferLayout2) LinearLayout transferLayout2; //icon

        @BindView(R.id.trip_origin_textView2) TextView origin2;
        @BindView(R.id.trip_destination_textView2) TextView destination2;
        @BindView(R.id.trip_departTime_textView2) TextView origTimeMin2;
        @BindView(R.id.trip_arrivalTime_textView2) TextView destTimeMin2;

        @BindView(R.id.trip_origin_textView3) TextView origin3;
        @BindView(R.id.trip_destination_textView3) TextView destination3;
        @BindView(R.id.trip_departTime_textView3) TextView origTimeMin3;
        @BindView(R.id.trip_arrivalTime_textView3) TextView destTimeMin3;

        @BindView(R.id.trip_totalTime_textView) TextView tripTime;
        @BindView(R.id.trip_date_textView) TextView origTimeDate;

        @BindView(R.id.trip_fare_textView) TextView fare;
        @BindView(R.id.trip_clipper_textView) TextView clipper;

        @BindView(R.id.trip_colored_line1) TextView coloredBar1;
        @BindView(R.id.trip_colored_line2) TextView coloredBar2;
        @BindView(R.id.trip_colored_line3) TextView coloredBar3;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private List<Trip> mTripList;
    private Context mContext;

    public TripRecyclerAdapter(Context context, List<Trip> tripList) {
        mContext = context;
        mTripList = tripList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTripList.get(position);

        if(trip.getOrigTimeDate() != null)
            holder.origTimeDate.setText(trip.getOrigTimeDate());
            Log.i("origTimeDate", trip.getOrigTimeDate());

        if(trip.getTripTime() != null)
            holder.tripTime.setText(trip.getTripTime());
            Log.i("tripTime", trip.getTripTime());

        if(trip.getClipper() != null)
            holder.clipper.setText(trip.getFares().getFare().get(0).getAmount());

        if(trip.getFare() != null)
            holder.fare.setText(trip.getFare());

        initTripLegs(trip, holder);
    }

    @Override
    public int getItemCount() {
        return mTripList.size();
    }

    private void initTripLegs(Trip trip, ViewHolder holder) {
        int length = trip.getLegList().size();
        if(length > 0) {
            initTextForLeg(LegOrder.FIRST_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(0).getLine(), holder, LegOrder.FIRST_LEG);
        }
        if(length > 1) {
            showLegView(LegOrder.SECOND_LEG, holder);
            initTextForLeg(LegOrder.SECOND_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(1).getLine(), holder, LegOrder.SECOND_LEG);
        }
        if(length > 2) {
            showLegView(LegOrder.THIRD_LEG, holder);
            initTextForLeg(LegOrder.THIRD_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(2).getLine(), holder, LegOrder.THIRD_LEG);
        }
    }

    private void showLegView(LegOrder leg, ViewHolder holder) {
        switch(leg) {
            case SECOND_LEG:
                holder.leg2Layout.setVisibility(View.VISIBLE);
                holder.transferLayout1.setVisibility(View.VISIBLE);
                //holder.transferIcon_iv1.setVisibility(View.VISIBLE);
                break;
            case THIRD_LEG:
                holder.leg3Layout.setVisibility(View.VISIBLE);
                holder.transferLayout2.setVisibility(View.VISIBLE);
                //holder.transferIcon_iv2.setVisibility(View.VISIBLE);
                break;
        }
    }

    public enum LegOrder {
        FIRST_LEG, SECOND_LEG, THIRD_LEG
    }

    private void initTextForLeg(LegOrder leg, ViewHolder holder, List<Trip> list) {
        switch(leg) {
            case FIRST_LEG:
                String origin1 = list.get(0).getLegList().get(0).getOrigin();
                String dest1 = list.get(0).getLegList().get(0).getDestination();
                Log.i("origin1", origin1);
                Log.i("dest1", dest1);
                holder.origin1.setText(getStationNameFromAbbr(origin1));
                holder.destination1.setText(getStationNameFromAbbr(dest1)); //todo: check this
                holder.origTimeMin1.setText(list.get(0).getLegList().get(0).getOrigTimeMin());
                holder.destTimeMin1.setText(list.get(0).getLegList().get(0).getDestTimeMin());
                break;
            case SECOND_LEG:
                String origin2 = list.get(1).getLegList().get(1).getOrigin();
                String dest2 = list.get(1).getLegList().get(1).getDestination();
                Log.i("origin2", origin2);
                Log.i("dest2", dest2);
                holder.origin2.setText(getStationNameFromAbbr(origin2));
                holder.destination2.setText(getStationNameFromAbbr(dest2)); //todo: check this
                holder.origTimeMin2.setText(list.get(1).getLegList().get(1).getOrigTimeMin());
                holder.destTimeMin2.setText(list.get(1).getLegList().get(1).getDestTimeMin());
                break;
            case THIRD_LEG:
                String origin3 = list.get(2).getLegList().get(2).getOrigin();
                String dest3 = list.get(2).getLegList().get(2).getDestination();
                Log.i("origin3", origin3);
                Log.i("dest3", dest3);
                holder.origin3.setText(getStationNameFromAbbr(origin3));
                holder.destination3.setText(getStationNameFromAbbr(dest3)); //todo: check this
                holder.origTimeMin3.setText(list.get(2).getLegList().get(2).getOrigTimeMin());
                holder.destTimeMin3.setText(list.get(2).getLegList().get(2).getDestTimeMin());
                break;
        }
    }

    private void setColoredBar(Context context, String route, ViewHolder holder, LegOrder leg) {
        BartRoutesUtils utils = new BartRoutesUtils(context);
        switch(leg) {
            case FIRST_LEG:
                utils.setLineBarByRoute(route, holder.coloredBar1);
                break;
            case SECOND_LEG:
                utils.setLineBarByRoute(route, holder.coloredBar2);
                break;
            case THIRD_LEG:
                utils.setLineBarByRoute(route, holder.coloredBar3);
                break;
        }
    }

    private String getStationNameFromAbbr(String abbr) {
        return StationList.stationMap.get(abbr.toLowerCase(Locale.US)); // keys are case sensitive
    }
}
