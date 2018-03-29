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
        new GetAdvisoryTask(getActivity(), getText(R.string.last_update).toString()).execute();
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

    private class GetAdvisoryTask extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private List<Advisory> mList;
        private String mMessage;
        private boolean mTimeBoolean;

        private GetAdvisoryTask(Context mContext, String message) {
            if(this.mContext == null)
                this.mContext = mContext;
            mList = new ArrayList<Advisory>();
            mMessage = message;

            // Check SharedPreferences for time setting
            SharedPreferences prefs = getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
            mTimeBoolean = prefs.getBoolean("TIME_KEY", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                FetchInputStream is = new FetchInputStream(mContext);
                InputStream in = is.connectToApi(ApiStringBuilder.getBSA());
                AdvisoryXmlParser parser = new AdvisoryXmlParser(mContext);
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
            if(result) {
                String time = "";
                for(Advisory adv : mList) {
                    if(adv.getTime() != null && mBsaTimeTv != null) {
                        if(mTimeBoolean) {
                            //time = adv.getTime();
                            time = TimeAndDate.format24hrTime(adv.getTime());
                        } else {
                            time = TimeAndDate.convertTo12Hr(adv.getTime());
                        }
                        String message = mMessage + " " + time;
                        mBsaTimeTv.setText(message);
                    }
                }
                AdvisoryViewAdapter adapter = new AdvisoryViewAdapter(mList, mContext);
                mBsaListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
            } else {
                mBsaTimeTv.setText(getResources().getString(R.string.update_unavailable));
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
