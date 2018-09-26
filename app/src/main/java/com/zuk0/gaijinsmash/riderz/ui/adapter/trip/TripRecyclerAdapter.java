package com.zuk0.gaijinsmash.riderz.ui.adapter.trip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils;
import com.zuk0.gaijinsmash.riderz.utils.debug.DebugController;

import java.util.List;

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView origin1;
        TextView destination1;
        TextView origTimeMin1;
        TextView destTimeMin1;

        TextView origin2;
        TextView destination2;
        TextView origTimeMin2;
        TextView destTimeMin2;

        TextView origin3;
        TextView destination3;
        TextView origTimeMin3;
        TextView destTimeMin3;

        TextView tripTime;
        TextView origTimeDate;

        TextView fare;
        TextView clipper;

        TextView coloredBar1;
        TextView coloredBar2;
        TextView coloredBar3;

        TextView departTitle2;
        TextView arriveTitle2;
        TextView departTitle3;
        TextView arriveTitle3;

        ImageView imageView1;
        ImageView imageView2;

        ViewHolder(View view) {
            super(view);

            origTimeDate = view.findViewById(R.id.trip_date_textView);

            // First Leg
            origin1 = view.findViewById(R.id.trip_origin_textView);
            destination1 = view.findViewById(R.id.trip_destination_textView);
            origTimeMin1 =   view.findViewById(R.id.trip_departTime_textView);
            coloredBar1 =   view.findViewById(R.id.trip_colored_line1);
            destTimeMin1 =   view.findViewById(R.id.trip_arrivalTime_textView);

            // Transfer Icon
            imageView1  =   view.findViewById(R.id.trip_imageView1);

            // Second Leg
            departTitle2 =  view.findViewById(R.id.trip_departTitle2);
            arriveTitle2 =  view.findViewById(R.id.trip_arriveTitle2);
            origin2 =       view.findViewById(R.id.trip_origin_textView2);
            destination2 =  view.findViewById(R.id.trip_destination_textView2);
            origTimeMin2 =  view.findViewById(R.id.trip_departTime_textView2);
            coloredBar2 =   view.findViewById(R.id.trip_colored_line2);
            destTimeMin2 =  view.findViewById(R.id.trip_arrivalTime_textView2);

            // Transfer Icon
            imageView2  =   view.findViewById(R.id.trip_imageView2);

            // Third Leg
            departTitle3 =  view.findViewById(R.id.trip_departTitle3);
            arriveTitle3 =  view.findViewById(R.id.trip_arriveTitle3);
            origin3 =       view.findViewById(R.id.trip_origin_textView3);
            destination3 =  view.findViewById(R.id.trip_destination_textView3);
            origTimeMin3 =  view.findViewById(R.id.trip_departTime_textView3);
            coloredBar3 =   view.findViewById(R.id.trip_colored_line3);
            destTimeMin3 =  view.findViewById(R.id.trip_arrivalTime_textView3);

            fare =          view.findViewById(R.id.trip_fare_textView);
            clipper =       view.findViewById(R.id.trip_clipper_textView);
            tripTime =      view.findViewById(R.id.trip_totalTime_textView);
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
            holder.clipper.setText(trip.getClipper());

        if(trip.getFare() != null)
            holder.fare.setText(trip.getFare());

        initTripLegs(trip, holder);
    }

    @Override
    public int getItemCount() {
        return mTripList.size();
    }

    //todo: change default visibility of Views to Gone in R.layout.list_row_trip
    private void initTripLegs(Trip trip, ViewHolder holder) {
        int length = trip.getLegList().size();
        if(length > 0) {
            // todo: set visibility of related views and coloredbars
            initTextForLeg(LegOrder.FIRST_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(0).getLine(), holder, LegOrder.FIRST_LEG);
        }
        if(length > 1) {
            initTextForLeg(LegOrder.SECOND_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(1).getLine(), holder, LegOrder.SECOND_LEG);
        } else {
            if(DebugController.DEBUG) {
                Log.i("Leg 2", "skipped");
            }
            holder.imageView1.setVisibility(View.GONE);
            holder.origin2.setVisibility(View.GONE);
            holder.destination2.setVisibility(View.GONE);
            holder.origTimeMin2.setVisibility(View.GONE);
            holder.destTimeMin2.setVisibility(View.GONE);
            holder.coloredBar2.setVisibility(View.GONE);
            holder.departTitle2.setVisibility(View.GONE);
            holder.arriveTitle2.setVisibility(View.GONE);
        }
        if(length > 2) {
            initTextForLeg(LegOrder.THIRD_LEG, holder, mTripList);
            setColoredBar(mContext, trip.getLegList().get(2).getLine(), holder, LegOrder.THIRD_LEG);
        } else {
            if(DebugController.DEBUG) {
                Log.i("Leg 3", "skipped");
            }
            holder.imageView2.setVisibility(View.GONE);
            holder.origin3.setVisibility(View.GONE);
            holder.destination3.setVisibility(View.GONE);
            holder.origTimeMin3.setVisibility(View.GONE);
            holder.destTimeMin3.setVisibility(View.GONE);
            holder.coloredBar3.setVisibility(View.GONE);
            holder.departTitle3.setVisibility(View.GONE);
            holder.arriveTitle3.setVisibility(View.GONE);
        }
    }

    public enum LegOrder {
        FIRST_LEG, SECOND_LEG, THIRD_LEG
    }

    private void initTextForLeg(LegOrder leg, ViewHolder holder, List<Trip> list) {
        switch(leg) {
            case FIRST_LEG:
                holder.origin1.setText(getStationNameFromAbbr(list.get(0).getOrigin()));
                holder.destination1.setText(getStationNameFromAbbr(list.get(0).getDestination())); //todo: check this
                holder.origTimeMin1.setText(list.get(0).getLegList().get(0).getOrigTimeMin());
                holder.destTimeMin1.setText(list.get(0).getLegList().get(0).getDestTimeMin());
                break;
            case SECOND_LEG:
                holder.origin2.setText(getStationNameFromAbbr(list.get(1).getOrigin()));
                holder.destination2.setText(getStationNameFromAbbr(list.get(1).getDestination())); //todo: check this
                holder.origTimeMin2.setText(list.get(1).getLegList().get(1).getOrigTimeMin());
                holder.destTimeMin2.setText(list.get(1).getLegList().get(1).getDestTimeMin());
                break;
            case THIRD_LEG:
                holder.origin3.setText(getStationNameFromAbbr(list.get(2).getOrigin()));
                holder.destination3.setText(getStationNameFromAbbr(list.get(2).getDestination())); //todo: check this
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
        StationList list = new StationList();
        return list.getStationMap().get(abbr.toLowerCase()); // keys are case sensitive
    }

}
