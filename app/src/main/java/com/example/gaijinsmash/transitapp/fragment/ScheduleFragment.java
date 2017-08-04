package com.example.gaijinsmash.transitapp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.internet.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.internet.InternetOperations;
import com.example.gaijinsmash.transitapp.model.Route;
import com.example.gaijinsmash.transitapp.xmlparser.RouteXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class ScheduleFragment extends Fragment {

    private Button searchBtn;
    private TextView textView;
    private AutoCompleteTextView departure;
    private AutoCompleteTextView arrival;
    private AutoCompleteTextView time;
    private AutoCompleteTextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mInflatedView = inflater.inflate(R.layout.schedule_view, container, false);

        departure = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView);
        arrival = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView2);
        time = (AutoCompleteTextView) mInflatedView.findViewById(R.id.time_editText);
        date = (AutoCompleteTextView) mInflatedView.findViewById(R.id.date_editText);
        searchBtn = (Button) mInflatedView.findViewById(R.id.schedule_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departingStation = departure.getText().toString();
                String arrivingStation = arrival.getText().toString();
                String departingTime = time.getText().toString();
                String departingDate = date.getText().toString();

                // get all input values and submit api call
                // return a list of routes
                new InternetTask().execute();
            }
        });

        //Inflate the layout for this fragment
        return mInflatedView;
    }

    //TODO: AsyncTask
    private class InternetTask extends AsyncTask<String[], Void, Boolean> {
        private RouteXMLParser routeXMLParser = new RouteXMLParser();
        private List<Route> routeList = null;

        @Override
        protected Boolean doInBackground(String[]...stations) {

            // Create the API Call
            ApiStringBuilder apiBuilder = new ApiStringBuilder();
            String uri = apiBuilder.getRoute(stations[0].toString(), stations[1].toString());
            try {
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
