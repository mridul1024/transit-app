package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.AdvisoryXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.utils.SharedPreferencesUtils;
import com.example.gaijinsmash.transitapp.utils.TimeAndDate;
import com.example.gaijinsmash.transitapp.view_adapter.AdvisoryViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;

public class HomeFragment extends Fragment {

    private TextView mBsaTimeTv = null;
    private ListView mBsaListView;
    private View mInflatedView;

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
        mBsaTimeTv = (TextView) mInflatedView.findViewById(R.id.home_view_timeTv);
        mBsaListView = mInflatedView.findViewById(R.id.advisory_listView);
        ImageView imageView = (ImageView) mInflatedView.findViewById(R.id.home_banner_imageView);
        if(TimeAndDate.isNightTime()) {
            imageView.setImageResource(R.drawable.sf_night);
        }
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    //todo: add holiday info

    private class GetAdvisoryTask extends AsyncTask<Void, Void, List<Advisory>> {
        private Context mContext;
        private List<Advisory> mList;
        private String mMessage;
        private boolean mTimeBoolean;

        public GetAdvisoryTask(Context mContext, String message) {
            if(this.mContext == null)
                this.mContext = mContext;
            mList = new ArrayList<Advisory>();
            mMessage = message;

            // Check SharedPreferences for time setting
            SharedPreferences prefs = getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
            mTimeBoolean = prefs.getBoolean("TIME_KEY", false);
        }

        @Override
        protected List<Advisory> doInBackground(Void... voids) {
            try {
                ApiStringBuilder builder = new ApiStringBuilder();
                FetchInputStream is = new FetchInputStream(mContext);
                InputStream in = is.connectToApi(builder.getBSA());
                AdvisoryXmlParser parser = new AdvisoryXmlParser(mContext);
                mList = parser.parse(in);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return mList;
        }

        protected void onPostExecute(List<Advisory> list) {
            Log.i("24hrFormatOn", Boolean.toString(mTimeBoolean));
            String time = "";
            for(Advisory adv : list) {
                if(adv.getTime() != null && mBsaTimeTv != null) {
                    if(mTimeBoolean) {
                        time = TimeAndDate.format24hrTime(adv.getTime());
                    } else {
                        time = TimeAndDate.convertTo12Hr(adv.getTime());
                    }
                    String message = mMessage + " " + time;
                    mBsaTimeTv.setText(message);
                }
            }
            AdvisoryViewAdapter adapter = new AdvisoryViewAdapter(list, mContext);
            mBsaListView.setAdapter(adapter);
        }
    }
}
