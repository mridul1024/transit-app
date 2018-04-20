package com.example.gaijinsmash.transitapp.view_adapter;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.style.TtsSpan;
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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class TripViewAdapter  extends ArrayAdapter<FullTrip> implements View.OnClickListener {

    private BartResultsFragment mFragment;
    private Context mContext;
    private static final boolean DEBUG = true;

    public TripViewAdapter(List<FullTrip> data, Context context, BartResultsFragment fragment) {
        super(context, R.layout.trip_list_row, data);
        mContext = context;
        mFragment = fragment;
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

    @NonNull
    @Override
    public View getView(int position, View convertView , @NonNull ViewGroup parent) {
        FullTrip fullTrip = getItem(position);
        TripViewAdapter.ViewHolder viewHolder;
        final View view;

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

        if (fullTrip != null) {
            viewHolder.origTimeDate.setText(fullTrip.getTrip().getOrigTimeDate());
            viewHolder.tripTime.setText(fullTrip.getTrip().getTripTime());
            viewHolder.clipper.setText(fullTrip.getFareList().get(0).getFareAmount());
            viewHolder.fare.setText(fullTrip.getFareList().get(1).getFareAmount());
            setTripViews2(fullTrip, viewHolder);
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }

    private void setTripViews2(FullTrip fullTrip, ViewHolder viewHolder) {
        int length = fullTrip.getLegList().size();
        Log.i("length of leg list", String.valueOf(length));
        if(length > 0) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 1).execute();
            setColoredBar(fullTrip.getLegList().get(0).getLine(), viewHolder, 1);
        }
        if(length > 1) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 2).execute();
            setColoredBar(fullTrip.getLegList().get(1).getLine(), viewHolder, 2);
        } else {
            if(DEBUG) {
                Log.i("Leg 2", "skipped");
            }
            viewHolder.imageView1.setVisibility(View.GONE);
            viewHolder.origin2.setVisibility(View.GONE);
            viewHolder.destination2.setVisibility(View.GONE);
            viewHolder.origTimeMin2.setVisibility(View.GONE);
            viewHolder.destTimeMin2.setVisibility(View.GONE);
            viewHolder.coloredBar2.setVisibility(View.GONE);
            viewHolder.departTitle2.setVisibility(View.GONE);
            viewHolder.arriveTitle2.setVisibility(View.GONE);
        }
        if(length > 2) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 3).execute();
            setColoredBar(fullTrip.getLegList().get(2).getLine(), viewHolder, 3);
        } else {
            if(DEBUG) {
                Log.i("Leg 3", "skipped");
            }
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

    private void setTripViews(FullTrip fullTrip, ViewHolder viewHolder) {
        int length = fullTrip.getLegList().size();

        if(length == 1) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 1).execute();
            setColoredBar(fullTrip.getLegList().get(0).getLine(), viewHolder, 1);

            // Remove Views
            viewHolder.imageView1.setVisibility(View.GONE);
            viewHolder.origin2.setVisibility(View.GONE);
            viewHolder.destination2.setVisibility(View.GONE);
            viewHolder.origTimeMin2.setVisibility(View.GONE);
            viewHolder.destTimeMin2.setVisibility(View.GONE);
            viewHolder.coloredBar2.setVisibility(View.GONE);
            viewHolder.departTitle2.setVisibility(View.GONE);
            viewHolder.arriveTitle2.setVisibility(View.GONE);

            viewHolder.imageView2.setVisibility(View.GONE);
            viewHolder.origin3.setVisibility(View.GONE);
            viewHolder.destination3.setVisibility(View.GONE);
            viewHolder.origTimeMin3.setVisibility(View.GONE);
            viewHolder.destTimeMin3.setVisibility(View.GONE);
            viewHolder.coloredBar3.setVisibility(View.GONE);
            viewHolder.departTitle3.setVisibility(View.GONE);
            viewHolder.arriveTitle3.setVisibility(View.GONE);
        }
        if(length == 2) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 1).execute();
            setColoredBar(fullTrip.getLegList().get(0).getLine(), viewHolder, 1);
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 2).execute();
            setColoredBar(fullTrip.getLegList().get(1).getLine(), viewHolder, 2);

            // Remove Views
            viewHolder.imageView2.setVisibility(View.GONE);
            viewHolder.origin3.setVisibility(View.GONE);
            viewHolder.destination3.setVisibility(View.GONE);
            viewHolder.origTimeMin3.setVisibility(View.GONE);
            viewHolder.destTimeMin3.setVisibility(View.GONE);
            viewHolder.coloredBar3.setVisibility(View.GONE);
            viewHolder.departTitle3.setVisibility(View.GONE);
            viewHolder.arriveTitle3.setVisibility(View.GONE);
        }
        if(length == 3) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 1).execute();
            setColoredBar(fullTrip.getLegList().get(0).getLine(), viewHolder, 1);
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 2).execute();
            setColoredBar(fullTrip.getLegList().get(1).getLine(), viewHolder, 2);
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 3).execute();
            setColoredBar(fullTrip.getLegList().get(2).getLine(), viewHolder, 3);
        }
    }

    private void setColoredBar(String route, ViewHolder viewHolder, int leg) {
        // Train Route Colors
        int blueLine = ContextCompat.getColor(mContext, R.color.bartBlueLine); //#0099cc
        int redLine = ContextCompat.getColor(mContext, R.color.bartRedLine); //#ff0000
        int greenLine = ContextCompat.getColor(mContext, R.color.bartGreenLine); //#339933
        int yellowLine = ContextCompat.getColor(mContext, R.color.bartYellowLine); //#ffff33
        int orangeLine = ContextCompat.getColor(mContext, R.color.bartOrangeLine); //#ff9933
        int grayLine = ContextCompat.getColor(mContext, R.color.bartOakAirport);
        int defaultLine = ContextCompat.getColor(mContext, R.color.bartDefault);

        if(leg == 1) {
            switch(route) {
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
        if(leg == 2) {
            switch(route) {
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
        }
        if(leg == 3) {
            switch(route) {
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
        }
    }

    private static class SetResultViewsTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<BartResultsFragment> mWeakRef;
        private String mOriginAbbr, mDestAbbr;
        private String mOriginResult, mDestResult;
        private int mLeg;
        private FullTrip mFullTrip;
        private ViewHolder mViewHolder;


        private SetResultViewsTask(BartResultsFragment context, ViewHolder viewHolder, FullTrip fullTrip, int leg) {
            this.mWeakRef = new WeakReference<>(context);
            this.mFullTrip = fullTrip;
            this.mViewHolder = viewHolder;
            this.mLeg = leg;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            BartResultsFragment context = mWeakRef.get(); //todo: should i use the context of the tripfragment?
            mOriginAbbr = mFullTrip.getLegList().get(mLeg - 1).getOrigin();
            mDestAbbr = mFullTrip.getLegList().get(mLeg - 1).getDestination();
            StationDatabase db = StationDatabase.getRoomDB(context.getActivity());

            //StationDbHelper helper = new StationDbHelper(context.getActivity());
            try {
                mOriginResult = db.getStationDAO().getStationByAbbr(mOriginAbbr).getName();
                mDestResult = db.getStationDAO().getStationByAbbr(mDestAbbr).getName();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            Log.i("Async ORIGIN", mOriginResult);
            Log.i("Async DESTINATION", mDestResult);

            return mOriginResult != null && mDestResult != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            BartResultsFragment context = mWeakRef.get();
            if(result) {
                switch(mLeg) {
                    case 1:
                        mViewHolder.origin.setText(mOriginResult);
                        mViewHolder.destination.setText(mDestResult);
                        mViewHolder.origTimeMin.setText(mFullTrip.getLegList().get(0).getOrigTimeMin());
                        mViewHolder.destTimeMin.setText(mFullTrip.getLegList().get(0).getDestTimeMin());
                        break;
                    case 2:
                        mViewHolder.origin2.setText(mOriginResult);
                        mViewHolder.destination2.setText(mDestResult);
                        mViewHolder.origTimeMin2.setText(mFullTrip.getLegList().get(1).getOrigTimeMin());
                        mViewHolder.destTimeMin2.setText(mFullTrip.getLegList().get(1).getDestTimeMin());
                        break;
                    case 3:
                        mViewHolder.origin3.setText(mOriginResult);
                        mViewHolder.destination3.setText(mDestResult);
                        mViewHolder.origTimeMin3.setText(mFullTrip.getLegList().get(2).getOrigTimeMin());
                        mViewHolder.destTimeMin3.setText(mFullTrip.getLegList().get(2).getDestTimeMin());
                        break;
                }
            } else {
                Toast.makeText(context.getActivity(), context.getString(R.string.error_try_again), Toast.LENGTH_LONG).show();
            }
        }
    }
}
