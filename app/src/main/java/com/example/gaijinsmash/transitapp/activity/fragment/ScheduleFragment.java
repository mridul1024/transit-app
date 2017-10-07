package com.example.gaijinsmash.transitapp.activity.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.activity.MainActivity;
import com.example.gaijinsmash.transitapp.database.BartStationDAO;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.model.bart.Route;
import com.example.gaijinsmash.transitapp.network.xmlparser.RouteXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import static android.app.DatePickerDialog.*;

/**
 * Created by ryanj on 6/30/2017.
 */

public class ScheduleFragment extends Fragment {

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private DatePicker datePicker;
    private Calendar calendar;
    private SimpleTimeZone timeZone;
    private SimpleDateFormat simpleDateFormat;
    private AutoCompleteTextView departureActv, arrivalActv;
    private EditText timeEt, dateEt;
    private Button searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mInflatedView = inflater.inflate(R.layout.schedule_view, container, false);

        //TODO: fetch a list of Stations for dropdown into "example"
        Resources res = getResources();
        String[] stations = res.getStringArray(R.array.stations_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(), android.R.layout.select_dialog_item, stations);

        // Departure UI
        departureActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView);
        departureActv.setThreshold(1); // will start working from first character
        departureActv.setAdapter(adapter);

        // Arrival UI
        arrivalActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView2);
        arrivalActv.setThreshold(1);
        arrivalActv.setAdapter(adapter);

        // Date UI
        dateEt = (EditText) mInflatedView.findViewById(R.id.date_editText);
        dateEt.setInputType(InputType.TYPE_NULL);
        dateEt.requestFocus();
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, day);
                        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        dateEt.setText(simpleDateFormat.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        // Time UI
        timeEt = (EditText) mInflatedView.findViewById(R.id.time_editText);
        timeEt.setInputType(InputType.TYPE_NULL);
        timeEt.requestFocus();
        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Current time is default value
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create and return a new instance of TimePickerDialog
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        String aMpM = "AM";
                        if(selectedHour > 11) {
                            aMpM = "PM";
                        }
                        timeEt.setText(String.format("%02d:%02d " + aMpM, selectedHour, selectedMinute));
                    }
                }, hour,minute,false); //True = 24 hour format
                timePickerDialog.setTitle(getString(R.string.time_title));
                timePickerDialog.show();
            }
        });

        searchBtn = (Button) mInflatedView.findViewById(R.id.schedule_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departingStation = departureActv.getText().toString();
                String arrivingStation = arrivalActv.getText().toString();
                // TODO: Convert Stations to abbreviated names, access DAO --> get abbr value with matching station name, return abbr string
                // time=h:mm+am/pm
                // date=<mm/dd/yyyy>
                String departingTime = timeEt.getText().toString();
                String departingDate = dateEt.getText().toString();
                String[] array = {departingStation, arrivingStation, departingTime, departingDate};
                new GetScheduleTask(getActivity()).execute(array);
            }
        });

        return mInflatedView;
    }

    private class GetScheduleTask extends AsyncTask<String[], Void, Boolean> {
        private RouteXMLParser routeXMLParser = null;
        private List<Route> routeList = null;
        private Context mContext;

        public GetScheduleTask(Context mContext) {
            if(this.mContext == null) {
                this.mContext = mContext;
            }
        }

        @Override
        protected Boolean doInBackground(String[]...stations) {

            // Create the API Call
            ApiStringBuilder apiBuilder = new ApiStringBuilder();



            String uri = apiBuilder.getSchedule(stations[0].toString(), stations[1].toString(), stations[2].toString(), stations[3].toString());
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
