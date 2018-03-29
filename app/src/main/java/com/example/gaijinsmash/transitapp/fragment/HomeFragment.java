package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.AdvisoryXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.utils.TimeAndDate;
import com.example.gaijinsmash.transitapp.view_adapter.AdvisoryViewAdapter;

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
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.home_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetAdvisoryTask(this, getText(R.string.last_update).toString()).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressBar = (ProgressBar) mInflatedView.findViewById(R.id.home_progressBar);
        mBsaTimeTv = (TextView) mInflatedView.findViewById(R.id.home_view_timeTv);
        mBsaListView = mInflatedView.findViewById(R.id.advisory_listView);
        ImageView imageView = (ImageView) mInflatedView.findViewById(R.id.home_banner_imageView);

        // Beach picture is shown by default
        int hour = TimeAndDate.getCurrentHour();
        if(hour < 6 || hour >= 21) {
            // show night picture
            imageView.setImageResource(R.drawable.sf_night);
        }
        if(hour >= 17 && hour < 21) {
            // show dusk picture
            imageView.setImageResource(R.drawable.sf_dusk);
        }
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    //todo: add holiday info

    private static class GetAdvisoryTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<HomeFragment> mHomeRef;
        private List<Advisory> mList;
        private String mMessage;
        private boolean mTimeBoolean;

        private GetAdvisoryTask(HomeFragment context, String message) {
            mList = new ArrayList<Advisory>();
            mMessage = message;
            mHomeRef = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HomeFragment homeFrag = mHomeRef.get();
            // Check SharedPreferences for time setting
            if(homeFrag != null) {
                SharedPreferences prefs = homeFrag.getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
                mTimeBoolean = prefs.getBoolean("TIME_KEY", false);
            }

            try {
                FetchInputStream is = new FetchInputStream(homeFrag.getActivity());
                InputStream in = is.connectToApi(ApiStringBuilder.getBSA());
                AdvisoryXmlParser parser = new AdvisoryXmlParser(homeFrag.getActivity());
                mList = parser.parse(in);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            if(mList != null) {
                return true;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            HomeFragment homeFrag = mHomeRef.get();
            if(result) {
                String time = "";
                for(Advisory adv : mList) {
                    if(adv.getTime() != null && homeFrag.mBsaTimeTv != null) {
                        if(mTimeBoolean) {
                            //time = adv.getTime();
                            time = TimeAndDate.format24hrTime(adv.getTime());
                        } else {
                            time = TimeAndDate.convertTo12Hr(adv.getTime());
                        }
                        String message = mMessage + " " + time;
                        homeFrag.mBsaTimeTv.setText(message);
                    }
                }
                AdvisoryViewAdapter adapter = new AdvisoryViewAdapter(mList, homeFrag.getActivity());
                homeFrag.mBsaListView.setAdapter(adapter);
                homeFrag.mProgressBar.setVisibility(View.GONE);
            } else {
                homeFrag.mBsaTimeTv.setText(homeFrag.getResources().getString(R.string.update_unavailable));
                homeFrag.mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
