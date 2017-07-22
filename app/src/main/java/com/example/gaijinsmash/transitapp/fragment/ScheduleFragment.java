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
import com.example.gaijinsmash.transitapp.internet.ApiBuilder;
import com.example.gaijinsmash.transitapp.internet.InternetOperations;

import java.io.InputStream;

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

                new InternetTask().execute();
            }
        });

        //Inflate the layout for this fragment
        return mInflatedView;
    }

    //TODO: show info on working Heater and A/C units

    //TODO: AsyncTask
    private class InternetTask extends AsyncTask<String[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(String[]...stations) {

            // send GET request to API
            ApiBuilder api = new ApiBuilder();
            String uri = api.getRoute(stations., stations[1]);
            InternetOperations ops = new InternetOperations();
            InputStream is = ops.connectToApi(uri);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}
