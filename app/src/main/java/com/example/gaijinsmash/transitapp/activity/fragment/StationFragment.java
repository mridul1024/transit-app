package com.example.gaijinsmash.transitapp.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.xmlparser.StationXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class StationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.station_view, container, false);

        return mInflatedView;
    }

    private class TestInternetTask extends AsyncTask<Void, Void, Boolean> {

        private StationXMLParser stationXMLParser = new StationXMLParser();
        private List<Station> stationList = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                stationList = stationXMLParser.testCall();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            if (stationList != null) {
                return true;
            } else {
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                // do something

            } else {
               // do something
            }
        }
    }
}
