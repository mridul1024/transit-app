package com.zuk0.gaijinsmash.riderz.xml_adapter.trip;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.debug.DebugController;
import com.zuk0.gaijinsmash.riderz.ui.fragment.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesHelper;
import com.zuk0.gaijinsmash.riderz.data.model.FullTrip;

import java.lang.ref.WeakReference;
import java.util.List;

public class TripViewAdapter extends ArrayAdapter<FullTrip> implements View.OnClickListener {

    private BartResultsFragment mFragment;

    public TripViewAdapter(List<FullTrip> data, Context context, BartResultsFragment fragment) {
        super(context, R.layout.list_row_trip, data);
        mFragment = fragment;
    }

    private static class ViewHolder {
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
            LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
            convertView = inflater.inflate(R.layout.list_row_trip, parent, false);

            viewHolder.origTimeDate =  convertView.findViewById(R.id.trip_date_textView);

            // First Leg
            viewHolder.origin1 =        convertView.findViewById(R.id.trip_origin_textView);
            viewHolder.destination1 =   convertView.findViewById(R.id.trip_destination_textView);
            viewHolder.origTimeMin1 =   convertView.findViewById(R.id.trip_departTime_textView);
            viewHolder.coloredBar1 =   convertView.findViewById(R.id.trip_colored_line1);
            viewHolder.destTimeMin1 =   convertView.findViewById(R.id.trip_arrivalTime_textView);

            // Transfer Icon
            viewHolder.imageView1  =   convertView.findViewById(R.id.trip_imageView1);

            // Second Leg
            viewHolder.departTitle2 =  convertView.findViewById(R.id.trip_departTitle2);
            viewHolder.arriveTitle2 =  convertView.findViewById(R.id.trip_arriveTitle2);
            viewHolder.origin2 =       convertView.findViewById(R.id.trip_origin_textView2);
            viewHolder.destination2 =  convertView.findViewById(R.id.trip_destination_textView2);
            viewHolder.origTimeMin2 =  convertView.findViewById(R.id.trip_departTime_textView2);
            viewHolder.coloredBar2 =   convertView.findViewById(R.id.trip_colored_line2);
            viewHolder.destTimeMin2 =  convertView.findViewById(R.id.trip_arrivalTime_textView2);

            // Transfer Icon
            viewHolder.imageView2  =   convertView.findViewById(R.id.trip_imageView2);

            // Third Leg
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
            view.setTag(viewHolder);
        } else {
            viewHolder = (TripViewAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }

        if (fullTrip != null) {
            viewHolder.origTimeDate.setText(fullTrip.getTrip().getOrigTimeDate());
            viewHolder.tripTime.setText(fullTrip.getTrip().getTripTime());
            viewHolder.clipper.setText(fullTrip.getFareList().get(0).getFareAmount());
            viewHolder.fare.setText(fullTrip.getFareList().get(1).getFareAmount());
            setTripViews(fullTrip, viewHolder);
        }
        return view;
    }

    private void setTripViews(FullTrip fullTrip, ViewHolder viewHolder) {
        int length = fullTrip.getLegList().size();
        if(length > 0) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 1).execute();
            setColoredBar(fullTrip.getLegList().get(0).getLine(), viewHolder, 1);
        }
        if(length > 1) {
            new SetResultViewsTask(mFragment, viewHolder, fullTrip, 2).execute();
            setColoredBar(fullTrip.getLegList().get(1).getLine(), viewHolder, 2);
        } else {
            if(DebugController.DEBUG) {
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
            if(DebugController.DEBUG) {
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

    private void setColoredBar(String route, ViewHolder viewHolder, int leg) {
        if(leg == 1) {
            BartRoutesHelper.setLineBarByRoute(mFragment.getActivity(), route, viewHolder.coloredBar1);
        }
        if(leg == 2) {
            BartRoutesHelper.setLineBarByRoute(mFragment.getActivity(), route, viewHolder.coloredBar2);
        }
        if(leg == 3) {
            BartRoutesHelper.setLineBarByRoute(mFragment.getActivity(), route, viewHolder.coloredBar3);
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
            BartResultsFragment context = mWeakRef.get();
            mOriginAbbr = mFullTrip.getLegList().get(mLeg - 1).getOrigin();
            mDestAbbr = mFullTrip.getLegList().get(mLeg - 1).getDestination();
            StationDatabase db = StationDatabase.getRoomDB(context.getActivity());
            try {
                mOriginResult = db.getStationDAO().getStationByAbbr(mOriginAbbr).getName();
                mDestResult = db.getStationDAO().getStationByAbbr(mDestAbbr).getName();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return mOriginResult != null && mDestResult != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            BartResultsFragment context = mWeakRef.get();
            if(result) {
                switch(mLeg) {
                    case 1:
                        mViewHolder.origin1.setText(mOriginResult);
                        mViewHolder.destination1.setText(mDestResult);
                        mViewHolder.origTimeMin1.setText(mFullTrip.getLegList().get(0).getOrigTimeMin());
                        mViewHolder.destTimeMin1.setText(mFullTrip.getLegList().get(0).getDestTimeMin());
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
