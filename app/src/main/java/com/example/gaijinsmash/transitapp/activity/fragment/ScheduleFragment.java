package com.example.gaijinsmash.transitapp.activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.BartStationDAO;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.model.bart.Route;
import com.example.gaijinsmash.transitapp.network.xmlparser.RouteXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class ScheduleFragment extends Fragment {

    private AutoCompleteTextView departureActv;
    private AutoCompleteTextView arrivalActv;
    private EditText timeEt;
    private EditText dateEt;
    private Button searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mInflatedView = inflater.inflate(R.layout.schedule_view, container, false);

        //TODO: fetch a list of Stations for dropdown into "example"
        String[] example = {"one","two","three"};

        // Store the list of Station objects
        BartStationDAO database = new BartStationDAO(getContext());
        List<Station> stationList = database.getAllStations();
        // Convert list to string array

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(), android.R.layout.select_dialog_item, example);

        departureActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView);
        departureActv.setThreshold(1); // will start working from first character
        departureActv.setAdapter(adapter);
        arrivalActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView2);
        arrivalActv.setThreshold(1);
        arrivalActv.setAdapter(adapter);
        timeEt = (EditText) mInflatedView.findViewById(R.id.time_editText);
        dateEt = (EditText) mInflatedView.findViewById(R.id.date_editText);
        searchBtn = (Button) mInflatedView.findViewById(R.id.schedule_button);

        // onClick, grab users input
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departingStation = departureActv.getText().toString();
                String arrivingStation = arrivalActv.getText().toString();
                String departingTime = timeEt.getText().toString();
                String departingDate = dateEt.getText().toString();
                String[] array = {departingStation, arrivingStation, departingTime, departingDate};
                new InternetTask(getContext()).execute(array);
            }
        });

        return mInflatedView;
    }


    //TODO: AsyncTask
    private class InternetTask extends AsyncTask<String[], Void, Boolean> {
        private RouteXMLParser routeXMLParser = null;
        private List<Route> routeList = null;
        private Context mContext;

        public InternetTask(Context mContext) {
            if(this.mContext == null) {
                this.mContext = mContext;
            }
        }

        @Override
        protected Boolean doInBackground(String[]...stations) {

            // Create the API Call
            ApiStringBuilder apiBuilder = new ApiStringBuilder();
            String uri = apiBuilder.getRoute(stations[0].toString(), stations[1].toString());

            //TODO: add date and time specs
            try {
                routeXMLParser = new RouteXMLParser(mContext);
                routeList = routeXMLParser.makeCall(uri);
            } catch (IOException e){
                e.printStackTrace();
                // TODO: Gracefully handle error for user
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            if (routeList != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // change fragment view to Results
                // display results in a custom list view
            }
        }
    }
}
