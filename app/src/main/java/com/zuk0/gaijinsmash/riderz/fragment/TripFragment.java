package com.zuk0.gaijinsmash.riderz.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.database.StationDbHelper;
import com.zuk0.gaijinsmash.riderz.model.bart.FullTrip;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesHelper;
import com.zuk0.gaijinsmash.riderz.utils.TimeAndDate;
import com.zuk0.gaijinsmash.riderz.xml_adapter.trip.TripXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripFragment extends Fragment {

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mSimpleDateFormat;
    private AutoCompleteTextView mDepartureActv, mArrivalActv;
    private TextInputEditText mTimeEt, mDateEt;
    private boolean mIs24HrTimeOn = false;
    private View mInflatedView;
    private TripFragment mTripFragment;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Initialize data here
        mIs24HrTimeOn = SharedPreferencesHelper.getTimePreference(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.view_trip, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // This data adapter is to provide a station list for Spinner and AutoCompleteTextView
        Resources res = getResources();
        String[] stations = res.getStringArray(R.array.stations_list);
        ArrayAdapter<String> textViewAdapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_selectable_list_item, stations);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stations);

        // Spinners (Drop Down List on Touch)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner departureSpinner = mInflatedView.findViewById(R.id.station_spinner1);
        departureSpinner.setAdapter(spinnerAdapter);
        departureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) departureSpinner.getSelectedView();
                String itemSelected = "";
                if (textView != null) {
                    itemSelected = textView.getText().toString();
                }
                mDepartureActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        Spinner arrivalSpinner = mInflatedView.findViewById(R.id.station_spinner2);
        arrivalSpinner.setAdapter(spinnerAdapter);
        arrivalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) arrivalSpinner.getSelectedView();
                String itemSelected = "";
                if(textView != null){
                    itemSelected = textView.getText().toString();
                }
                mArrivalActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Departure UI
        mDepartureActv = mInflatedView.findViewById(R.id.trip_autoCompleteTextView);
        mDepartureActv.setThreshold(1); // will start working from first character
        mDepartureActv.setAdapter(textViewAdapter);

        // Arrival UI
        mArrivalActv = mInflatedView.findViewById(R.id.trip_autoCompleteTextView2);
        mArrivalActv.setThreshold(1);
        mArrivalActv.setAdapter(textViewAdapter);

        // Date UI
        mDateEt = mInflatedView.findViewById(R.id.date_editText);
        mDateEt.setText(getText(R.string.currentDate));
        mDateEt.setInputType(InputType.TYPE_NULL);
        mDateEt.requestFocus();
        mDateEt.setOnClickListener(view1 -> {
            Calendar newCalendar = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(getActivity(), (view11, year, month, day) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                mDateEt.setText(mSimpleDateFormat.format(newDate.getTime()));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        });

        // Time UI
        mTimeEt = mInflatedView.findViewById(R.id.time_editText);
        mTimeEt.setText(getText(R.string.currentTime));
        mTimeEt.setInputType(InputType.TYPE_NULL);
        mTimeEt.requestFocus();
        mTimeEt.setOnClickListener(view12 -> {
            // Current time is default value
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            // Create and return a new instance of TimePickerDialog
            mTimePickerDialog = new TimePickerDialog(getActivity(), (view121, selectedHour, selectedMinute) -> {
                // Returned value is always 24hr so conversion is necessary
                if(mIs24HrTimeOn) {
                    String formattedTime = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute);
                    mTimeEt.setText(formattedTime);
                } else {
                    String convertedTime = TimeAndDate.convertTo12HrForTrip(selectedHour + ":" + selectedMinute);
                    mTimeEt.setText(convertedTime);
                }
            }, hour,minute,mIs24HrTimeOn); //True = 24 hour format on TimePicker only
            mTimePickerDialog.setTitle(getString(R.string.time_title));
            mTimePickerDialog.show();
        });

        // Submit Button
        Button searchBtn = mInflatedView.findViewById(R.id.trip_button);
        searchBtn.setOnClickListener(view13 -> {
            String departingStation = mDepartureActv.getText().toString();
            String arrivingStation = mArrivalActv.getText().toString();

            // TIME
            String preformatTime = mTimeEt.getText().toString();
            String departingTime = getTimeForTripSearch(preformatTime);

            // DATE
            String departingDate = mDateEt.getText().toString();

            attemptTripSearch(departingTime, departingDate, departingStation, arrivingStation);
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTripFragment = this;
        setRetainInstance(true); // handles screen orientation
    }

    //---------------------------------------------------------------------------------------------
    // Helpers
    //---------------------------------------------------------------------------------------------

    private String getTimeForTripSearch(String preformatTime) {
        String result;
        if (!preformatTime.equals("Now") && mIs24HrTimeOn) {
            String convertedTime = TimeAndDate.convertTo12HrForTrip(preformatTime);
            result = TimeAndDate.formatTime(convertedTime);
        } else if (!preformatTime.equals("Now") && !mIs24HrTimeOn) {
            result = TimeAndDate.formatTime(preformatTime); // time=h:mm+AM/PM
        } else {
            result = preformatTime;
        }
        return result;
    }

    public void attemptTripSearch(String time, String date, String origin, String destination) {
        if(validateUserInput(time, date, origin, destination)) {
            new GetTripTask(mTripFragment, origin, destination, date, time).execute();
        }
    }

    public boolean doesStationExist(String station) {
        String[] stations = getResources().getStringArray(R.array.stations_list);
        for(String x : stations) {
            if(station.equals(x)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateUserInput(String departingTime, String departingDate, String departingStation, String arrivingStation) {
        //todo : show error field on screen instead of toast
        //todo: check if whitespace is at end of string before continuing
        // check if all fields are filled
        if (departingStation.isEmpty() || arrivingStation.isEmpty() || departingDate.isEmpty() || departingTime.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.error_form_completion), Toast.LENGTH_LONG).show();
            return false;
        }
        // check if origin and destination are different
        else if (departingStation.equals(arrivingStation)) {
            Toast.makeText(getActivity(), getString(R.string.error_form_completion2), Toast.LENGTH_LONG).show();
            return false;
        }
        //check if both stations are matching in strings resource
        else if (!doesStationExist(departingStation) || !doesStationExist(arrivingStation)) {
            Toast.makeText(getActivity(), getString(R.string.error_station_not_found), Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

    private static class GetTripTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<TripFragment> mWeakRef;
        private TripXMLParser tripXMLParser = null;
        private List<FullTrip> mFullTripList = null;
        private String mDepartingStn, mArrivingStn, mDepartAbbr, mArriveAbbr, mDate, mTime;

        private GetTripTask(TripFragment context, String departingStn, String arrivingStn, String date, String time) {
            mWeakRef = new WeakReference<>(context);
            mDepartingStn = departingStn;
            mArrivingStn = arrivingStn;
            mDate = date;
            mTime = time;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            TripFragment frag = mWeakRef.get();
            boolean result = false;
            // Create the API Call
            try {
                if (frag != null) {
                    mDepartAbbr = StationDbHelper.getAbbrFromDb(frag.getActivity(), mDepartingStn);
                    mArriveAbbr = StationDbHelper.getAbbrFromDb(frag.getActivity(), mArrivingStn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String uri = BartApiStringBuilder.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                if (frag != null) {
                    tripXMLParser = new TripXMLParser(frag.getActivity());
                    mFullTripList = tripXMLParser.getList(uri);
                }
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
            }
            if (mFullTripList != null) {
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TripFragment frag = mWeakRef.get();
            if (result) {
                // add list to parcelable bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("FullTripList", (ArrayList<? extends Parcelable>) mFullTripList);
                bundle.putString("Origin", mDepartAbbr);
                bundle.putString("Destination", mArriveAbbr);

                // Switch to BartResultsFragment
                Fragment newFrag = new BartResultsFragment();
                newFrag.setArguments(bundle);
                FragmentManager fm;
                if (frag != null) {
                    fm = frag.getFragmentManager();
                    fm.beginTransaction().replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
                }
            } else {
                if (frag != null) {
                    Toast.makeText(frag.getActivity(),frag.getResources().getString(R.string.error_try_again),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
