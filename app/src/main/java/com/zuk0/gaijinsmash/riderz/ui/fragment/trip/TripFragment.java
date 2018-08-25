package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
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
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDbHelper;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;
import com.zuk0.gaijinsmash.riderz.utils.debug.DebugController;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.FullTrip;
import com.zuk0.gaijinsmash.riderz.utils.BartApiUtils;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.ui.adapter.trip.TripXMLParser;

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
        mIs24HrTimeOn = SharedPreferencesUtils.getTimePreference(getActivity());
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
                    String convertedTime = TimeDateUtils.convertTo12HrForTrip(selectedHour + ":" + selectedMinute);
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
            String convertedTime = TimeDateUtils.convertTo12HrForTrip(preformatTime);
            result = TimeDateUtils.formatTime(convertedTime);
        } else if (!preformatTime.equals("Now") && !mIs24HrTimeOn) {
            result = TimeDateUtils.formatTime(preformatTime); // time=h:mm+AM/PM
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

    public enum TripBundle {
        ORIGIN("Origin"), DESTINATION("Destination"), FULLTRIP_LIST("FullTripList"), TRAIN_HEADERS("TrainHeaders");
        private String value;

        TripBundle(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }
    private static class GetTripTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<TripFragment> mWeakRef;
        private TripXMLParser tripXMLParser = null;
        private List<FullTrip> mFullTripList = null;
        private String mDepartingStn, mArrivingStn, mDepartAbbr, mArriveAbbr, mDate, mTime;
        private List<String> mTrainHeaders;

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
            StationDbHelper db = new StationDbHelper(frag.getActivity());
            try {
                mDepartAbbr = db.getAbbrFromDb(mDepartingStn);
                mArriveAbbr = db.getAbbrFromDb(mArrivingStn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.closeDb();
            }
            String uri = BartApiUtils.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                tripXMLParser = new TripXMLParser(frag.getActivity());
                mFullTripList = tripXMLParser.getList(uri);
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
                bundle.putParcelableArrayList(TripBundle.FULLTRIP_LIST.getValue(), (ArrayList<? extends Parcelable>) mFullTripList);
                bundle.putString(TripBundle.ORIGIN.getValue(), mDepartAbbr);
                bundle.putString(TripBundle.DESTINATION.getValue(), mArriveAbbr);

                mTrainHeaders = new ArrayList<>();

                for(FullTrip fullTrip : mFullTripList) {
                    mTrainHeaders.add(fullTrip.getLegList().get(0).getTrainHeadStation());
                    if(DebugController.DEBUG) Log.d("trainHeader added", fullTrip.getLegList().get(0).getTrainHeadStation());
                }
                bundle.putStringArrayList(TripBundle.TRAIN_HEADERS.getValue(), (ArrayList<String>) mTrainHeaders);
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
