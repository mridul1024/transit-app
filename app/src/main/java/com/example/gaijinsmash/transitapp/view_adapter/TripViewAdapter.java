package com.example.gaijinsmash.transitapp.view_adapter;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.fragment.BartResultsFragment;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;
import com.example.gaijinsmash.transitapp.model.bart.Station;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TripViewAdapter  extends ArrayAdapter<FullTrip> implements View.OnClickListener {
    private BartResultsFragment mFragment;
    private Context mContext;

    public TripViewAdapter(List<FullTrip> data, Context context, BartResultsFragment fragment) {
        super(context, R.layout.trip_list_row, data);
        this.mContext = context;
        this.mFragment = fragment;
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

        TextView coloredBar1;
        TextView coloredBar2;
        TextView coloredBar3;

        TextView departTitle2;
        TextView arriveTitle2;
        TextView departTitle3;
        TextView arriveTitle3;

        ImageView imageView1;
        ImageView imageView2;
    }

    @Override
    public void onClick(View view) {
        //int position  = (Integer) view.getTag();
        //Object object = getItem(position);
        //FullTrip trip = (FullTrip) object;
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, View convertView , @NonNull ViewGroup parent) {
        FullTrip fullTrip = getItem(position);
        TripViewAdapter.ViewHolder viewHolder;
        final View view;

        //TODO REVIEW NULL CHECKS - may be causing infinite loop.

        if(convertView == null) {
            viewHolder = new TripViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.trip_list_row, parent, false);

            viewHolder.origTimeDate =  convertView.findViewById(R.id.trip_date_textView);

            viewHolder.origin =        convertView.findViewById(R.id.trip_origin_textView);
            viewHolder.destination =   convertView.findViewById(R.id.trip_destination_textView);
            viewHolder.origTimeMin =   convertView.findViewById(R.id.trip_departTime_textView);
            viewHolder.coloredBar1 =   convertView.findViewById(R.id.trip_colored_line1);
            viewHolder.destTimeMin =   convertView.findViewById(R.id.trip_arrivalTime_textView);

            viewHolder.imageView1  =   convertView.findViewById(R.id.trip_imageView1);

            viewHolder.departTitle2 =  convertView.findViewById(R.id.trip_departTitle2);
            viewHolder.arriveTitle2 =  convertView.findViewById(R.id.trip_arriveTitle2);
            viewHolder.origin2 =       convertView.findViewById(R.id.trip_origin_textView2);
            viewHolder.destination2 =  convertView.findViewById(R.id.trip_destination_textView2);
            viewHolder.origTimeMin2 =  convertView.findViewById(R.id.trip_departTime_textView2);
            viewHolder.coloredBar2 =   convertView.findViewById(R.id.trip_colored_line2);
            viewHolder.destTimeMin2 =  convertView.findViewById(R.id.trip_arrivalTime_textView2);

            viewHolder.imageView2  =   convertView.findViewById(R.id.trip_imageView2);

            viewHolder.departTitle3 =  convertView.findViewById(R.id.trip_departTitle3);
            viewHolder.arriveTitle3 =  convertView.findViewById(R.id.trip_arriveTitle3);
            viewHolder.origin3 =       convertView.findViewById(R.id.trip_origin_textView3);
            viewHolder.destination3 =  convertView.findViewById(R.id.trip_destination_textView3);
            viewHolder.origTimeMin3 =  convertView.findViewById(R.id.trip_departTime_textView3);
            viewHolder.coloredBar3 =   convertView.findViewById(R.id.trip_colored_line3);
            viewHolder.destTimeMin3 =  convertView.findViewById(R.id.trip_arrivalTime_textView3);

            viewHolder.fare =          convertView.findViewById(R.id.trip_fare_textView);
            viewHolder.clipper =       convertView.findViewById(R.id.trip_clipper_textView);
            viewHolder.tripTime =      convertView.findViewById(R.id.trip_totalTime_textView);

            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TripViewAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        if (fullTrip != null && fullTrip.getTrip() != null) {
            viewHolder.origTimeDate.setText(fullTrip.getTrip().getOrigTimeDate());
            viewHolder.tripTime.setText(fullTrip.getTrip().getTripTime());
        }
        if (fullTrip != null) {

            viewHolder.origin.setText(fullTrip.getLegList().get(0).getOrigin());
            viewHolder.destination.setText(fullTrip.getLegList().get(0).getDestination());

            //new GetStationNameFromDBTask(mFragment, fullTrip.getLegList().get(0).getOrigin(),
              //      fullTrip.getLegList().get(0).getDestination(), 1).execute();
        }
        if (fullTrip != null) {
            viewHolder.origTimeMin.setText(fullTrip.getLegList().get(0).getOrigTimeMin());
        }
        if (fullTrip != null) {
            viewHolder.destTimeMin.setText(fullTrip.getLegList().get(0).getDestTimeMin());
        }

        int blueLine = ContextCompat.getColor(mContext, R.color.bartBlueLine); //#0099cc
        int redLine = ContextCompat.getColor(mContext, R.color.bartRedLine); //#ff0000
        int greenLine = ContextCompat.getColor(mContext, R.color.bartGreenLine); //#339933
        int yellowLine = ContextCompat.getColor(mContext, R.color.bartYellowLine); //#ffff33
        int orangeLine = ContextCompat.getColor(mContext, R.color.bartOrangeLine); //#ff9933
        int grayLine = ContextCompat.getColor(mContext, R.color.bartOakAirport);
        int defaultLine = ContextCompat.getColor(mContext, R.color.bartDefault);

        if (fullTrip != null) {
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
        }

        if (fullTrip != null) {
            if (fullTrip.getLegList().size() == 2) {
                //viewHolder.origin2.setText(fullTrip.getLegList().get(1).getOrigin());
                //viewHolder.destination2.setText(fullTrip.getLegList().get(1).getDestination());
                //new GetStationNameFromDBTask(mFragment, fullTrip.getLegList().get(1).getOrigin(),
                       // fullTrip.getLegList().get(1).getDestination(), 2).execute();
                viewHolder.origin2.setText(fullTrip.getLegList().get(1).getOrigin());
                viewHolder.destination2.setText(fullTrip.getLegList().get(1).getDestination());
                viewHolder.origTimeMin2.setText(fullTrip.getLegList().get(1).getOrigTimeMin());
                viewHolder.destTimeMin2.setText(fullTrip.getLegList().get(1).getDestTimeMin());
                switch(fullTrip.getLegList().get(1).getLine()) {
                    case "ROUTE 1": viewHolder.coloredBar2.setBackgroundColor(yellowLine);
                        break;
                    case "ROUTE 2": viewHolder.coloredBar2.setBackgroundColor(yellowLine);
                        break;
                    case "ROUTE 3": viewHolder.coloredBar2.setBackgroundColor(orangeLine);
                        break;
                    case "ROUTE 4": viewHolder.coloredBar2.setBackgroundColor(orangeLine);
                        break;
                    case "ROUTE 5": viewHolder.coloredBar2.setBackgroundColor(greenLine);
                        break;
                    case "ROUTE 6": viewHolder.coloredBar2.setBackgroundColor(greenLine);
                        break;
                    case "ROUTE 7": viewHolder.coloredBar2.setBackgroundColor(redLine);
                        break;
                    case "ROUTE 8":  viewHolder.coloredBar2.setBackgroundColor(redLine);
                        break;
                    case "ROUTE 11": viewHolder.coloredBar2.setBackgroundColor(blueLine);
                        break;
                    case "ROUTE 12": viewHolder.coloredBar2.setBackgroundColor(blueLine);
                        break;
                    case "ROUTE 19": viewHolder.coloredBar2.setBackgroundColor(grayLine);
                        break;
                    case "ROUTE 20": viewHolder.coloredBar2.setBackgroundColor(grayLine);
                        break;
                    default:        viewHolder.coloredBar2.setBackgroundColor(defaultLine);
                        break;
                }
            } else { // Hide Text Views if no data is present, bottom textviews will automatically move up because of LinearLayout behavior
                viewHolder.imageView1.setVisibility(View.GONE);
                viewHolder.origin2.setVisibility(View.GONE);
                viewHolder.destination2.setVisibility(View.GONE);
                viewHolder.origTimeMin2.setVisibility(View.GONE);
                viewHolder.destTimeMin2.setVisibility(View.GONE);
                viewHolder.coloredBar2.setVisibility(View.GONE);
                viewHolder.departTitle2.setVisibility(View.GONE);
                viewHolder.arriveTitle2.setVisibility(View.GONE);
            }
        }

        if (fullTrip != null) {
            if (fullTrip.getLegList().size() == 3) {
                //viewHolder.origin2.setText(fullTrip.getLegList().get(2).getOrigin());
                //viewHolder.destination2.setText(fullTrip.getLegList().get(2).getDestination());
                //new GetStationNameFromDBTask(mFragment, fullTrip.getLegList().get(2).getOrigin(),
                      //  fullTrip.getLegList().get(2).getDestination(), 3).execute();
                viewHolder.origin3.setText(fullTrip.getLegList().get(2).getOrigin());
                viewHolder.destination3.setText(fullTrip.getLegList().get(2).getDestination());
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
                viewHolder.imageView2.setVisibility(View.GONE);
                viewHolder.origin3.setVisibility(View.GONE);
                viewHolder.destination3.setVisibility(View.GONE);
                viewHolder.origTimeMin3.setVisibility(View.GONE);
                viewHolder.destTimeMin3.setVisibility(View.GONE);
                viewHolder.coloredBar3.setVisibility(View.GONE);
                viewHolder.departTitle3.setVisibility(View.GONE);
                viewHolder.arriveTitle3.setVisibility(View.GONE);
            }
        }

        if (fullTrip != null) {
            viewHolder.fare.setText(fullTrip.getFareList().get(1).getFareAmount());
            viewHolder.clipper.setText(fullTrip.getFareList().get(0).getFareAmount());
        }
        return convertView;
    }

    private static class GetStationNameFromDBTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<BartResultsFragment> mWeakRef;
        private String mOriginAbbr, mDestAbbr;
        private String mOriginResult, mDestResult;
        private int mTextViewGroup = 0;

        private GetStationNameFromDBTask(BartResultsFragment context, String originAbbr, String destAbbr, int textViewGroup) {
            mWeakRef = new WeakReference<>(context);
            this.mOriginAbbr = originAbbr;
            this.mDestAbbr = destAbbr;
            this.mTextViewGroup = textViewGroup;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            BartResultsFragment context = mWeakRef.get();
            StationDatabase db = StationDatabase.getRoomDB(context.getActivity());

            /*
            mOriginResult = db.getStationDAO().getStationByAbbr(mOriginAbbr).getName();
            Log.i("ORIGIN", mOriginResult);
            mDestResult = db.getStationDAO().getStationByAbbr(mDestAbbr).getName();
            Log.i("DESTINATION", mDestResult);
            */
            return mOriginResult != null && mDestResult != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            BartResultsFragment context = mWeakRef.get();
            if(result) {
                TextView origin = null;
                TextView destination = null;

                switch(mTextViewGroup) {
                    case 1:
                        origin = context.getActivity().findViewById(R.id.trip_origin_textView);
                        destination = context.getActivity().findViewById(R.id.trip_destination_textView);
                        origin.setText(mOriginResult);
                        destination.setText(mDestResult);
                    case 2:
                        origin = context.getActivity().findViewById(R.id.trip_origin_textView2);
                        destination = context.getActivity().findViewById(R.id.trip_destination_textView2);
                        origin.setText(mOriginResult);
                        destination.setText(mDestResult);
                    case 3:
                        origin = context.getActivity().findViewById(R.id.trip_origin_textView3);
                        destination = context.getActivity().findViewById(R.id.trip_destination_textView3);
                        origin.setText(mOriginResult);
                        destination.setText(mDestResult);
                }
            } else {
                Toast.makeText(context.getActivity(), context.getResources().getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
