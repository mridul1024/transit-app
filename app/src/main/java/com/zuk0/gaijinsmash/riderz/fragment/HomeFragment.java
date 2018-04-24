package com.zuk0.gaijinsmash.riderz.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.model.bart.Advisory;
import com.zuk0.gaijinsmash.riderz.network.FetchInputStream;
import com.zuk0.gaijinsmash.riderz.network.xmlparser.AdvisoryXmlParser;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;
import com.zuk0.gaijinsmash.riderz.utils.TimeAndDate;
import com.zuk0.gaijinsmash.riderz.view_adapter.AdvisoryViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView mBsaTimeTv = null;
    private ListView mBsaListView;
    private View mInflatedView;
    private ProgressBar mProgressBar;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.home_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressBar = mInflatedView.findViewById(R.id.home_progressBar);
        mBsaTimeTv = mInflatedView.findViewById(R.id.home_view_timeTv);
        mBsaListView = mInflatedView.findViewById(R.id.advisory_listView);
        ImageView imageView = mInflatedView.findViewById(R.id.home_banner_imageView);

        int hour = TimeAndDate.getCurrentHour();
        if(hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(getActivity())
                    .load(R.drawable.sf_night)
                    .into(imageView);
        } else if(hour >= 17) {
            // show dusk picture
            Glide.with(getActivity())
                    .load(R.drawable.sf_dusk)
                    .into(imageView);
        } else {
            Glide.with(getActivity())
                    .load(R.drawable.sf_day)
                    .into(imageView);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        new GetAdvisoryTask( this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

    private static class GetAdvisoryTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<HomeFragment> mHomeRef;
        private List<Advisory> mList;
        private boolean mTimeBoolean;

        private GetAdvisoryTask(HomeFragment context) {
            mList = new ArrayList<>();
            mHomeRef = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HomeFragment homeFrag = mHomeRef.get();
            // Check SharedPreferences for time setting
            if(homeFrag != null && homeFrag.isAdded()) {
                SharedPreferences prefs = homeFrag.getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
                mTimeBoolean = prefs.getBoolean("TIME_KEY", false);
                try {
                    FetchInputStream is = new FetchInputStream(homeFrag.getActivity());
                    InputStream in = is.connectToApi(BartApiStringBuilder.getBSA());
                    AdvisoryXmlParser parser = new AdvisoryXmlParser(homeFrag.getActivity());
                    mList = parser.parse(in); //todo: fix this
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return mList != null;
        }

        protected void onPostExecute(Boolean result) {
            HomeFragment homeFrag = mHomeRef.get();
            if(result) {
                String time;
                for(Advisory adv : mList) {
                    if(adv.getTime() != null && homeFrag != null) {
                        if(mTimeBoolean) {
                            time = TimeAndDate.format24hrTime(adv.getTime());
                            Log.i("getTime", adv.getTime());
                            Log.i("new time", time);
                        } else {
                            time = TimeAndDate.convertTo12Hr(adv.getTime());
                            Log.i("getTime", adv.getTime());
                            Log.i("new time", time);
                        }
                        String message = homeFrag.getResources().getString(R.string.last_update) + " " + time;
                        homeFrag.mBsaTimeTv.setText(message);
                    }
                }

                AdvisoryViewAdapter adapter;
                if (homeFrag != null && homeFrag.isAdded()) {
                    adapter = new AdvisoryViewAdapter(mList, homeFrag.getActivity());
                    homeFrag.mBsaListView.setAdapter(adapter);
                    homeFrag.mProgressBar.setVisibility(View.GONE);
                }
            } else {
                if(homeFrag != null && homeFrag.isAdded()) {
                    homeFrag.mBsaTimeTv.setText(homeFrag.getResources().getString(R.string.update_unavailable));
                    homeFrag.mProgressBar.setVisibility(View.GONE);
                }
            }
        }
    }
}
