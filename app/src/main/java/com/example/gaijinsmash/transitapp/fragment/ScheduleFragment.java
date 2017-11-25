package com.example.gaijinsmash.transitapp.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.model.bart.Route;
import com.example.gaijinsmash.transitapp.network.xmlparser.RouteXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.app.DatePickerDialog.*;

public class ScheduleFragment extends Fragment {

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mSimpleDateFormat;
    private AutoCompleteTextView mDepartureActv, mArrivalActv;
    private EditText mTimeEt, mDateEt;
    private Button mSearchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mInflatedView = inflater.inflate(R.layout.schedule_view, container, false);

        // TODO: update nav menus
        DrawerLayout layout = (DrawerLayout) mInflatedView.findViewById(R.id.drawer_layout);

        Resources res = getResources();
        String[] stations = res.getStringArray(R.array.stations_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(), android.R.layout.select_dialog_item, stations);

        // Departure UI
        mDepartureActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView);
        mDepartureActv.setThreshold(1); // will start working from first character
        mDepartureActv.setAdapter(adapter);

        // Arrival UI
        mArrivalActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView2);
        mArrivalActv.setThreshold(1);
        mArrivalActv.setAdapter(adapter);

        // Date UI
        mDateEt = (EditText) mInflatedView.findViewById(R.id.date_editText);
        mDateEt.setInputType(InputType.TYPE_NULL);
        mDateEt.requestFocus();
        mDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, day);
                        mSimpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        mDateEt.setText(mSimpleDateFormat.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });

        // Time UI
        mTimeEt = (EditText) mInflatedView.findViewById(R.id.time_editText);
        mTimeEt.setInputType(InputType.TYPE_NULL);
        mTimeEt.requestFocus();
        mTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Current time is default value
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create and return a new instance of TimePickerDialog
                mTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        String aMpM = "AM";
                        if(selectedHour > 11) {
                            aMpM = "PM";
                        }
                        mTimeEt.setText(String.format("%02d:%02d " + aMpM, selectedHour, selectedMinute));
                    }
                }, hour,minute,false); //True = 24 hour format
                mTimePickerDialog.setTitle(getString(R.string.time_title));
                mTimePickerDialog.show();
            }
        });

        mSearchBtn = (Button) mInflatedView.findViewById(R.id.schedule_button);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departingStation = mDepartureActv.getText().toString();
                String arrivingStation = mArrivalActv.getText().toString();
                String departingTime = mTimeEt.getText().toString();    // time=h:mm+am/pm
                String departingDate = mDateEt.getText().toString();    // date=<mm/dd/yyyy>
                if(departingStation.isEmpty() || arrivingStation.isEmpty() || departingDate.isEmpty() || departingDate.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.error_form_completion), Toast.LENGTH_LONG);
                } else {
                    String[] array = {departingStation, arrivingStation, departingTime, departingDate};
                    new GetScheduleTask(getActivity()).execute(array);
                }
            }
        });
        return mInflatedView;
    }

    public String getAbbrFromDb(String stationName) throws XmlPullParserException, IOException {
        StationDatabase db = StationDatabase.getRoomDB(getActivity());
        Station station = db.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }

    private class GetScheduleTask extends AsyncTask<String[], Void, Boolean> {
        private RouteXMLParser routeXMLParser = null;
        private List<Route> routeList = null;
        private Context mContext;
        private String mDepart, mArrive, mTime, mDate, mDepartAbbr, mArriveAbbr;

        public GetScheduleTask(Context mContext) {
            if(this.mContext == null) {
                this.mContext = mContext;
            }
        }

        public void setFields(String depart, String arrive, String time, String date) {
            this.mDepart = depart;
            this.mArrive = arrive;
            this.mTime = time;
            this.mDate = date;
        }

        @Override
        protected Boolean doInBackground(String[]...stations) {

            // Create the API Call
            try {
                mDepartAbbr = getAbbrFromDb(stations[0].toString());
                mArriveAbbr = getAbbrFromDb(stations[1].toString());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String uri = ApiStringBuilder.getDetailedRoute(mDepartAbbr, mArriveAbbr, stations[2].toString(), stations[3].toString());
            try {
                routeXMLParser = new RouteXMLParser(mContext);
                routeList = routeXMLParser.getList(uri);
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
