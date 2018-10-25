package com.zuk0.gaijinsmash.riderz.ui.adapter.trip;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripBinding;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils;

import java.util.List;
import java.util.Locale;

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListRowTripBinding mDataBinding;

        ViewHolder(final ListRowTripBinding viewBinding) {
            super(viewBinding.getRoot());
            this.mDataBinding = viewBinding;
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
        ListRowTripBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_row_trip, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTripList.get(position);

        if(trip.getOrigTimeDate() != null)
            holder.mDataBinding.tripDateTextView.setText(trip.getOrigTimeDate());
            Log.i("origTimeDate", trip.getOrigTimeDate());

        if(trip.getTripTime() != null)
            holder.mDataBinding.tripTotalTimeTextView.setText(trip.getTripTime());
            Log.i("tripTime", trip.getTripTime());

        if(trip.getClipper() != null)
            holder.mDataBinding.tripClipperTextView.setText(trip.getFares().getFare().get(0).getAmount());

        if(trip.getFare() != null)
            holder.mDataBinding.tripFareTextView.setText(trip.getFare());

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
                holder.mDataBinding.resultsLeg2.setVisibility(View.VISIBLE);
                holder.mDataBinding.tripTransferLayout1.setVisibility(View.VISIBLE);
                //holder.transferIcon_iv1.setVisibility(View.VISIBLE);
                break;
            case THIRD_LEG:
                holder.mDataBinding.resultsLeg3.setVisibility(View.VISIBLE);
                holder.mDataBinding.tripTransferLayout2.setVisibility(View.VISIBLE);
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
                holder.mDataBinding.tripOriginTextView.setText(getStationNameFromAbbr(origin1));
                holder.mDataBinding.tripDestinationTextView.setText(getStationNameFromAbbr(dest1)); //todo: check this
                holder.mDataBinding.tripDepartTimeTextView.setText(list.get(0).getLegList().get(0).getOrigTimeMin());
                holder.mDataBinding.tripArrivalTimeTextView.setText(list.get(0).getLegList().get(0).getDestTimeMin());
                break;
            case SECOND_LEG:
                String origin2 = list.get(1).getLegList().get(1).getOrigin();
                String dest2 = list.get(1).getLegList().get(1).getDestination();
                Log.i("origin2", origin2);
                Log.i("dest2", dest2);
                holder.mDataBinding.tripOriginTextView2.setText(getStationNameFromAbbr(origin2));
                holder.mDataBinding.tripDestinationTextView2.setText(getStationNameFromAbbr(dest2)); //todo: check this
                holder.mDataBinding.tripDepartTimeTextView2.setText(list.get(1).getLegList().get(1).getOrigTimeMin());
                holder.mDataBinding.tripArrivalTimeTextView2.setText(list.get(1).getLegList().get(1).getDestTimeMin());
                break;
            case THIRD_LEG:
                String origin3 = list.get(2).getLegList().get(2).getOrigin();
                String dest3 = list.get(2).getLegList().get(2).getDestination();
                Log.i("origin3", origin3);
                Log.i("dest3", dest3);
                holder.mDataBinding.tripOriginTextView3.setText(getStationNameFromAbbr(origin3));
                holder.mDataBinding.tripDestinationTextView3.setText(getStationNameFromAbbr(dest3)); //todo: check this
                holder.mDataBinding.tripDepartTimeTextView3.setText(list.get(2).getLegList().get(2).getOrigTimeMin());
                holder.mDataBinding.tripArrivalTimeTextView3.setText(list.get(2).getLegList().get(2).getDestTimeMin());
                break;
        }
    }

    private void setColoredBar(Context context, String route, ViewHolder holder, LegOrder leg) {
        switch(leg) {
            case FIRST_LEG:
                BartRoutesUtils.setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine1);
                break;
            case SECOND_LEG:
                BartRoutesUtils.setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine2);
                break;
            case THIRD_LEG:
                BartRoutesUtils.setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine3);
                break;
        }
    }

    private String getStationNameFromAbbr(String abbr) {
        return StationList.stationMap.get(abbr.toLowerCase(Locale.US)); // keys are case sensitive
    }
}
