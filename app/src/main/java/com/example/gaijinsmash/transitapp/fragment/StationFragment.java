package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.view_adapter.StationViewAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;



public class StationFragment extends Fragment {

    private ListView mListView;
    private ProgressBar mProgressBar;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Initialize data here
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.station_view, container, false);
        mListView = inflatedView.findViewById(R.id.station_listView);
        mProgressBar = (ProgressBar) inflatedView.findViewById(R.id.station_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        new GetStationsTask(getActivity()).execute();

        return inflatedView;
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private class GetStationsTask extends AsyncTask<Void, Void, List<Station>> {

        private StationXMLParser stationXMLParser = null;
        private List<Station> stationList = null;
        private Context mContext;

        public GetStationsTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Station> doInBackground(Void... voids) {
            try {
                if(stationList == null && stationXMLParser == null) {
                    stationXMLParser = new StationXMLParser(mContext);
                    stationList = stationXMLParser.getList(ApiStringBuilder.getAllStations());
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return stationList;
        }

        protected void onPostExecute(List<Station> stations) {
            if(stationList != null) {
                StationViewAdapter adapter = new StationViewAdapter(stationList, mContext);
                mListView.setAdapter(adapter);
            } else {
                // TODO: Handle error gracefully for use - possibly use a cute img?
                Toast.makeText(mContext, getString(R.string.cannot_do_this), Toast.LENGTH_LONG);
                Log.e("onPostExecute()", "stationList is NULL");
            }
        }
    }
}
