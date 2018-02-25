package com.example.gaijinsmash.transitapp.view_adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripViewAdapter  extends ArrayAdapter<FullTrip> implements View.OnClickListener {
    private List<FullTrip> mTripList = new ArrayList<FullTrip>();
    private Context mContext;

    public TripViewAdapter(List<FullTrip> data, Context context) {
        super(context, R.layout.trip_list_row, data);
        this.mTripList = data;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView origin;
        TextView destination;
        TextView origTimeMin;
        TextView destTimeMin;

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
        //TextView co2;

        TextView coloredBar1;
        TextView coloredBar2;
        TextView coloredBar3;

        TextView departTitle2;
        TextView arriveTitle2;
        TextView departTitle3;
        TextView arriveTitle3;
    }

    @Override
    public void onClick(View view) {
        int position  = (Integer) view.getTag();
        Object object = getItem(position);
        FullTrip trip = (FullTrip) object;

        switch (view.getId()) {
            // TODO: do something on click?
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        FullTrip fullTrip = getItem(position);
        TripViewAdapter.ViewHolder viewHolder;
        final View view;
        if(convertView == null) {
            viewHolder = new TripViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.trip_list_row, parent, false);

            viewHolder.origTimeDate = (TextView) convertView.findViewById(R.id.trip_date_textView);

            viewHolder.origin =       (TextView) convertView.findViewById(R.id.trip_origin_textView);
            viewHolder.destination =  (TextView) convertView.findViewById(R.id.trip_destination_textView);
            viewHolder.origTimeMin =  (TextView) convertView.findViewById(R.id.trip_departTime_textView);
            viewHolder.destTimeMin =  (TextView) convertView.findViewById(R.id.trip_arrivalTime_textView);

            viewHolder.origin2 =      (TextView) convertView.findViewById(R.id.trip_origin_textView2);
            viewHolder.destination2 = (TextView) convertView.findViewById(R.id.trip_destination_textView2);
            viewHolder.origTimeMin2 = (TextView) convertView.findViewById(R.id.trip_departTime_textView2);
            viewHolder.destTimeMin2 = (TextView) convertView.findViewById(R.id.trip_arrivalTime_textView2);

            viewHolder.origin3 =      (TextView) convertView.findViewById(R.id.trip_origin_textView3);
            viewHolder.destination3 = (TextView) convertView.findViewById(R.id.trip_destination_textView3);
            viewHolder.origTimeMin3 = (TextView) convertView.findViewById(R.id.trip_departTime_textView3);
            viewHolder.destTimeMin3 = (TextView) convertView.findViewById(R.id.trip_arrivalTime_textView3);

            viewHolder.fare =         (TextView) convertView.findViewById(R.id.trip_fare_textView);
            viewHolder.clipper =      (TextView) convertView.findViewById(R.id.trip_clipper_textView);

            viewHolder.tripTime =     (TextView) convertView.findViewById(R.id.trip_totalTime_textView);

            viewHolder.coloredBar1 =  (TextView) convertView.findViewById(R.id.trip_colored_line1);
            viewHolder.coloredBar2 =  (TextView) convertView.findViewById(R.id.trip_colored_line2);
            viewHolder.coloredBar3 =  (TextView) convertView.findViewById(R.id.trip_colored_line3);
            viewHolder.departTitle2 = (TextView) convertView.findViewById(R.id.trip_departTitle2);
            viewHolder.arriveTitle2 = (TextView) convertView.findViewById(R.id.trip_arriveTitle2);
            viewHolder.departTitle3 = (TextView) convertView.findViewById(R.id.trip_departTitle3);
            viewHolder.arriveTitle3 = (TextView) convertView.findViewById(R.id.trip_arriveTitle3);

            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TripViewAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        if(fullTrip.getTrip() != null) {
            viewHolder.origTimeDate.setText(fullTrip.getTrip().getOrigTimeDate());
            viewHolder.tripTime.setText(fullTrip.getTrip().getTripTime());
        }
        new GetStationNameFromDBTask(mContext, fullTrip.getLegList().get(0).getOrigin(), viewHolder.origin,
                fullTrip.getLegList().get(0).getDestination(), viewHolder.destination).execute();
        viewHolder.origTimeMin.setText(fullTrip.getLegList().get(0).getOrigTimeMin());
        viewHolder.destTimeMin.setText(fullTrip.getLegList().get(0).getDestTimeMin());

        int blueLine = ContextCompat.getColor(mContext, R.color.bartBlueLine); //#0099cc
        int redLine = ContextCompat.getColor(mContext, R.color.bartRedLine); //#ff0000
        int greenLine = ContextCompat.getColor(mContext, R.color.bartGreenLine); //#339933
        int yellowLine = ContextCompat.getColor(mContext, R.color.bartYellowLine); //#ffff33
        int orangeLine = ContextCompat.getColor(mContext, R.color.bartOrangeLine); //#ff9933
        int grayLine = ContextCompat.getColor(mContext, R.color.bartOakAirport);
        int defaultLine = ContextCompat.getColor(mContext, R.color.bartDefault);

        switch(fullTrip.getLegList().get(0).getLine()) {
            case "ROUTE 1": viewHolder.coloredBar1.setBackgroundColor(yellowLine);
                            break;
            case "ROUTE 2": viewHolder.coloredBar1.setBackgroundColor(yellowLine);
                            break;
            case "ROUTE 3": viewHolder.coloredBar1.setBackgroundColor(orangeLine);
                            break;
            case "ROUTE 4": viewHolder.coloredBar1.setBackgroundColor(orangeLine);
                            break;
            case "ROUTE 5": viewHolder.coloredBar1.setBackgroundColor(greenLine);
                            break;
            case "ROUTE 6": viewHolder.coloredBar1.setBackgroundColor(greenLine);
                            break;
            case "ROUTE 7": viewHolder.coloredBar1.setBackgroundColor(redLine);
                            break;
            case "ROUTE 8":  viewHolder.coloredBar1.setBackgroundColor(redLine);
                            break;
            case "ROUTE 11": viewHolder.coloredBar1.setBackgroundColor(blueLine);
                            break;
            case "ROUTE 12": viewHolder.coloredBar1.setBackgroundColor(blueLine);
                            break;
            case "ROUTE 19": viewHolder.coloredBar1.setBackgroundColor(grayLine);
                            break;
            case "ROUTE 20": viewHolder.coloredBar1.setBackgroundColor(grayLine);
                            break;
            default:        viewHolder.coloredBar1.setBackgroundColor(defaultLine);
                            break;
        }

        if (fullTrip.getLegList().size() > 1) {
            //viewHolder.origin2.setText(fullTrip.getLegList().get(1).getOrigin());
            //viewHolder.destination2.setText(fullTrip.getLegList().get(1).getDestination());
            new GetStationNameFromDBTask(mContext, fullTrip.getLegList().get(1).getOrigin(), viewHolder.origin2,
                    fullTrip.getLegList().get(1).getDestination(), viewHolder.destination2).execute();

            viewHolder.origTimeMin2.setText(fullTrip.getLegList().get(1).getOrigTimeMin());
            viewHolder.destTimeMin2.setText(fullTrip.getLegList().get(1).getDestTimeMin());
            switch(fullTrip.getLegList().get(1).getLine()) {
                case "ROUTE 1": viewHolder.coloredBar1.setBackgroundColor(yellowLine);
                    break;
                case "ROUTE 2": viewHolder.coloredBar1.setBackgroundColor(yellowLine);
                    break;
                case "ROUTE 3": viewHolder.coloredBar1.setBackgroundColor(orangeLine);
                    break;
                case "ROUTE 4": viewHolder.coloredBar1.setBackgroundColor(orangeLine);
                    break;
                case "ROUTE 5": viewHolder.coloredBar1.setBackgroundColor(greenLine);
                    break;
                case "ROUTE 6": viewHolder.coloredBar1.setBackgroundColor(greenLine);
                    break;
                case "ROUTE 7": viewHolder.coloredBar1.setBackgroundColor(redLine);
                    break;
                case "ROUTE 8":  viewHolder.coloredBar1.setBackgroundColor(redLine);
                    break;
                case "ROUTE 11": viewHolder.coloredBar1.setBackgroundColor(blueLine);
                    break;
                case "ROUTE 12": viewHolder.coloredBar1.setBackgroundColor(blueLine);
                    break;
                case "ROUTE 19": viewHolder.coloredBar1.setBackgroundColor(grayLine);
                    break;
                case "ROUTE 20": viewHolder.coloredBar1.setBackgroundColor(grayLine);
                    break;
                default:        viewHolder.coloredBar1.setBackgroundColor(defaultLine);
                    break;
            }
        } else { // Hide Text Views if no data is present, bottom textviews will automatically move up because of LinearLayout behavior
            viewHolder.origin2.setVisibility(View.GONE);
            viewHolder.destination2.setVisibility(View.GONE);
            viewHolder.origTimeMin2.setVisibility(View.GONE);
            viewHolder.destTimeMin2.setVisibility(View.GONE);
            viewHolder.coloredBar2.setVisibility(View.GONE);
            viewHolder.departTitle2.setVisibility(View.GONE);
            viewHolder.arriveTitle2.setVisibility(View.GONE);
        }

        if (fullTrip.getLegList().size() > 2) {
            //viewHolder.origin2.setText(fullTrip.getLegList().get(2).getOrigin());
            //viewHolder.destination2.setText(fullTrip.getLegList().get(2).getDestination());
            new GetStationNameFromDBTask(mContext, fullTrip.getLegList().get(2).getOrigin(), viewHolder.origin3,
                    fullTrip.getLegList().get(2).getDestination(), viewHolder.destination3).execute();

            viewHolder.origTimeMin2.setText(fullTrip.getLegList().get(2).getOrigTimeMin());
            viewHolder.destTimeMin2.setText(fullTrip.getLegList().get(2).getDestTimeMin());
            switch(fullTrip.getLegList().get(2).getLine()) {
                case "ROUTE 1": viewHolder.coloredBar3.setBackgroundColor(yellowLine);
                    break;
                case "ROUTE 2": viewHolder.coloredBar3.setBackgroundColor(yellowLine);
                    break;
                case "ROUTE 3": viewHolder.coloredBar3.setBackgroundColor(orangeLine);
                    break;
                case "ROUTE 4": viewHolder.coloredBar3.setBackgroundColor(orangeLine);
                    break;
                case "ROUTE 5": viewHolder.coloredBar3.setBackgroundColor(greenLine);
                    break;
                case "ROUTE 6": viewHolder.coloredBar3.setBackgroundColor(greenLine);
                    break;
                case "ROUTE 7": viewHolder.coloredBar3.setBackgroundColor(redLine);
                    break;
                case "ROUTE 8":  viewHolder.coloredBar3.setBackgroundColor(redLine);
                    break;
                case "ROUTE 11": viewHolder.coloredBar3.setBackgroundColor(blueLine);
                    break;
                case "ROUTE 12": viewHolder.coloredBar3.setBackgroundColor(blueLine);
                    break;
                case "ROUTE 19": viewHolder.coloredBar3.setBackgroundColor(grayLine);
                    break;
                case "ROUTE 20": viewHolder.coloredBar3.setBackgroundColor(grayLine);
                    break;
                default:        viewHolder.coloredBar3.setBackgroundColor(defaultLine);
                    break;
            }
        } else { // Hide Text Views if no data is present and  move bottom TextViews up
            viewHolder.origin3.setVisibility(View.GONE);
            viewHolder.destination3.setVisibility(View.GONE);
            viewHolder.origTimeMin3.setVisibility(View.GONE);
            viewHolder.destTimeMin3.setVisibility(View.GONE);
            viewHolder.coloredBar3.setVisibility(View.GONE);
            viewHolder.departTitle3.setVisibility(View.GONE);
            viewHolder.arriveTitle3.setVisibility(View.GONE);
        }

        viewHolder.fare.setText(fullTrip.getFareList().get(0).getFareAmount());
        viewHolder.clipper.setText(fullTrip.getFareList().get(1).getFareAmount());

        return convertView;
    }

    private class GetStationNameFromDBTask extends AsyncTask<Void, Void, Boolean> {

        private String mOriginAbbr, mDestAbbr;
        private String mOriginResult, mDestResult;
        private TextView mOriginTextView, mDestTextView;
        private Context mContext;

        private String mDestination;

        public GetStationNameFromDBTask(Context context, String originAbbr, TextView originTextView,
                                            String destAbbr, TextView destTextView) {
            this.mContext = context;
            this.mOriginAbbr = originAbbr;
            this.mOriginTextView = originTextView;
            this.mDestAbbr = destAbbr;
            this.mDestTextView = destTextView;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            Boolean result = false;
            StationDbHelper db = new StationDbHelper(mContext);
            try {
                mOriginResult = db.getNameFromAbbr(mOriginAbbr);
                mDestResult = db.getNameFromAbbr(mDestAbbr);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mOriginResult.isEmpty() && mDestResult.isEmpty()) {
                Log.i("Error with Database", "doInBackground() of GetStationNameFromDBTask");
            } else {
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                mOriginTextView.setText(mOriginResult);
                mDestTextView.setText(mDestResult);
            } else {
                Log.e("Error", "onPostExecute() of GetStationNameFromDBTask");
            }
        }
    }
}
