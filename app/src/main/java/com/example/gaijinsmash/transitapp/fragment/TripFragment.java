package com.example.gaijinsmash.transitapp.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.xmlparser.TripXMLParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;
import com.example.gaijinsmash.transitapp.utils.TimeAndDate;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.DatePickerDialog.OnDateSetListener;

public class TripFragment extends Fragment {

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mSimpleDateFormat;
    private AutoCompleteTextView mDepartureActv, mArrivalActv;
    private EditText mTimeEt, mDateEt;
    private Button mSearchBtn;
    boolean mIs24HrTimeOn = false;
    private View mInflatedView;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Initialize data here
        setRetainInstance(true); // handles screen orientation

        // Check SharedPreferences for time setting
        SharedPreferences prefs = getActivity().getSharedPreferences("TIME_PREFS", Context.MODE_PRIVATE);
        mIs24HrTimeOn = prefs.getBoolean("TIME_KEY", false); // false = default value if Key is not found
        Log.i("checkbox_value is ", String.valueOf(mIs24HrTimeOn));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.trip_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DrawerLayout layout = (DrawerLayout) mInflatedView.findViewById(R.id.drawer_layout);

        // This data adapter is to provide a station list for Spinner and AutoCompleteTextView
        Resources res = getResources();
        String[] stations = res.getStringArray(R.array.stations_list);
        ArrayAdapter<String> textViewAdapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_selectable_list_item, stations);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stations);

        // Spinners (Drop Down List on Touch)
        Spinner departureSpinner = (Spinner) mInflatedView.findViewById(R.id.station_spinner1);
        Spinner arrivalSpinner = (Spinner) mInflatedView.findViewById(R.id.station_spinner2);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        departureSpinner.setAdapter(spinnerAdapter);
        departureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) departureSpinner.getSelectedView();
                String itemSelected = textView.getText().toString();
                mDepartureActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        arrivalSpinner.setAdapter(spinnerAdapter);
        arrivalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) arrivalSpinner.getSelectedView();
                String itemSelected = textView.getText().toString();
                mArrivalActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        // Departure UI
        mDepartureActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView);
        mDepartureActv.setThreshold(1); // will start working from first character
        mDepartureActv.setAdapter(textViewAdapter);

        // Arrival UI
        mArrivalActv = (AutoCompleteTextView) mInflatedView.findViewById(R.id.schedule_autoCompleteTextView2);
        mArrivalActv.setThreshold(1);
        mArrivalActv.setAdapter(textViewAdapter);

        // Date UI
        mDateEt = (EditText) mInflatedView.findViewById(R.id.date_editText);
        mDateEt.setText(getText(R.string.currentDate));
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
                        mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        mDateEt.setText(mSimpleDateFormat.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });

        // Time UI
        mTimeEt = (EditText) mInflatedView.findViewById(R.id.time_editText);
        mTimeEt.setText(getText(R.string.currentTime));
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
                        // Returned value is always 24hr - so conversion is necessary
                        if(mIs24HrTimeOn) {
                            String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                            Log.i("formattedTime",formattedTime);
                            mTimeEt.setText(formattedTime);

                        } else {
                            String converttedTime = TimeAndDate.convertTo12HrForTrip(selectedHour + ":" + selectedMinute);
                            Log.i("converttedTime", converttedTime);
                            mTimeEt.setText(converttedTime);
                        }
                    }
                }, hour,minute,mIs24HrTimeOn); //True = 24 hour format on TimePicker only
                mTimePickerDialog.setTitle(getString(R.string.time_title));
                mTimePickerDialog.show();
            }
        });

        // Submit Button
        mSearchBtn = (Button) mInflatedView.findViewById(R.id.schedule_button);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departingStation = mDepartureActv.getText().toString();
                Log.i("departing: ", departingStation);
                String arrivingStation = mArrivalActv.getText().toString();
                Log.i("arriving: ", arrivingStation);

                // TIME
                String preformatTime = mTimeEt.getText().toString();
                String departingTime = "";

                if(!preformatTime.equals("Now") && mIs24HrTimeOn) {
                    String convertedTime = TimeAndDate.convertTo12Hr(preformatTime);
                    Log.d("convertedTime : ", convertedTime);
                    departingTime = TimeAndDate.formatTime(convertedTime);
                } else if(!preformatTime.equals("Now") && !mIs24HrTimeOn){
                    departingTime = TimeAndDate.formatTime(preformatTime); // time=h:mm+AM/PM
                } else {
                    departingTime = preformatTime;
                }
                Log.d("departingTime : ", departingTime);

                // DATE
                String departingDate = mDateEt.getText().toString();
                Log.d("departingDate : ", departingDate);

                if(departingStation.isEmpty() || arrivingStation.isEmpty() || departingDate.isEmpty() || departingDate.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.error_form_completion), Toast.LENGTH_LONG).show();
                } else {
                    //String[] array = {departingStation, arrivingStation, departingTime, departingDate};
                    new GetScheduleTask(getActivity(), departingStation, arrivingStation, departingDate, departingTime).execute();
                }
            }
        });
    }

    public String getAbbrFromDb(String stationName) throws XmlPullParserException, IOException {
        Log.i("STATION NAME: ", stationName);
        StationDatabase db = StationDatabase.getRoomDB(getActivity());
        Station station = db.getStationDAO().getStationByName(stationName);
        Log.i("Station", station.getName());
        Log.i("ABBR: ", station.getAbbreviation());
        return station.getAbbreviation();
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

    private class GetScheduleTask extends AsyncTask<Void, Void, Boolean> {
        private TripXMLParser routeXMLParser = null;
        private List<FullTrip> mTripList = null;
        private Context mContext;
        private String mDepartingStn, mArrivingStn, mDepartAbbr, mArriveAbbr, mDate, mTime;

        public GetScheduleTask(Context context, String departingStn, String arrivingStn, String date, String time) {
            if(this.mContext == null) {
                mContext = context;
                mDate = date;
                mTime = time;
                mDepartingStn = departingStn;
                mArrivingStn = arrivingStn;
            }
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            boolean result = false;
            // Create the API Call
            try {
                StationDbHelper.initStationDb(mContext);
                mDepartAbbr = getAbbrFromDb(mDepartingStn);
                mArriveAbbr = getAbbrFromDb(mArrivingStn);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String uri = ApiStringBuilder.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                routeXMLParser = new TripXMLParser(mContext);
                mTripList = routeXMLParser.getList(uri);
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
                Toast.makeText(mContext, getResources().getText(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
            if (mTripList != null) {
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // add list to parcelable bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("TripList", (ArrayList<? extends Parcelable>) mTripList);
                // Switch to BartResultsFragment
                Fragment newFrag = new BartResultsFragment();
                newFrag.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragmentContent, newFrag).commit();
            }
        }
    }
}
