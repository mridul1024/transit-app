package com.zuk0.gaijinsmash.riderz.ui.fragment.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TripFragment extends Fragment {

    @BindView(R.id.trip_arrival_autoCompleteTextView) AutoCompleteTextView mArrivalActv;
    @BindView(R.id.trip_departure_autoCompleteTextView) AutoCompleteTextView mDepartureActv;
    @BindView(R.id.trip_date_editText) TextInputEditText mDateEt;
    @BindView(R.id.trip_time_editText) TextInputEditText mTimeEt;
    @BindView(R.id.station_spinner1) Spinner mDepartureSpinner;
    @BindView(R.id.station_spinner2) Spinner mArrivalSpinner;
    @BindView(R.id.trip_button) Button mSearchButton;

    private TripViewModel mViewModel;
    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mSimpleDateFormat;
    private boolean mIs24HrTimeOn = false;
    private String[] mStationsList;
    private ArrayAdapter<String> mSpinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.view_trip, container, false);
        ButterKnife.bind(this, inflatedView);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initTimePreference(getActivity());
        initStationsList();
        initSpinnerAdapter();
        initDepartingSpinner(mSpinnerAdapter);
        initArrivingSpinner(mSpinnerAdapter);
        initDatePicker();
        initTimePicker();
        initTextInputEditors();
        initSearchButton();
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
    }

    private void initTimePreference(Context context) {
        mIs24HrTimeOn = SharedPreferencesUtils.getTimePreference(context);
    }

    private void initStationsList() {
        // This data is to provide a station list for Spinner and AutoCompleteTextView
        mStationsList = getResources().getStringArray(R.array.stations_list);
    }

    private void initSpinnerAdapter() {
        mSpinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, mStationsList);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    }

    private void initDepartingSpinner(ArrayAdapter<String> adapter) {
        mDepartureSpinner.setAdapter(adapter);
        mDepartureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) mDepartureSpinner.getSelectedView();
                String itemSelected = "";
                if (textView != null) {
                    itemSelected = textView.getText().toString();
                }
                mDepartureActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initArrivingSpinner(ArrayAdapter<String> adapter) {
        mArrivalSpinner.setAdapter(adapter);
        mArrivalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) mArrivalSpinner.getSelectedView();
                String itemSelected = "";
                if(textView != null){
                    itemSelected = textView.getText().toString();
                }
                mArrivalActv.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initDatePicker() {
        mDateEt.setText(getText(R.string.currentDate));
        mDateEt.setInputType(InputType.TYPE_NULL);
        mDateEt.requestFocus();
        mDateEt.setOnClickListener(view1 -> {
            Calendar newCalendar = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (view11, year, month, day) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                mDateEt.setText(mSimpleDateFormat.format(newDate.getTime()));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        });
    }

    private void initTimePicker() {
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
    }

    private void initTextInputEditors() {
        ArrayAdapter<String> textViewAdapter = new ArrayAdapter<> (Objects.requireNonNull(getActivity()), android.R.layout.simple_selectable_list_item, mStationsList);

        mDepartureActv.setThreshold(1); // will start working from first character
        mDepartureActv.setAdapter(textViewAdapter);

        mArrivalActv.setThreshold(1);
        mArrivalActv.setAdapter(textViewAdapter);
    }

    private void initSearchButton() {
        mSearchButton.setOnClickListener(view13 -> {

            /*
                NOTE: stations have not been formatted to their abbreviated versions yet.
                From the BartResultsFragment, take String values from Bundle
                and convert them appropriately before making API call
             */
            String departingStation = mDepartureActv.getText().toString();
            String arrivingStation = mArrivalActv.getText().toString();

            // TIME
            String preformatTime = mTimeEt.getText().toString();
            String departingTime = mViewModel.getTimeForTripSearch(preformatTime, mIs24HrTimeOn);

            // DATE
            String departingDate = mDateEt.getText().toString();

            attemptTripSearch(departingStation, arrivingStation, departingDate, departingTime);
        });
    }

    private boolean doesStationExist(String station) {
        String[] stations = getResources().getStringArray(R.array.stations_list);

        //todo: optimize search
        for(String x : stations) {
            if(station.equals(x)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateUserInput(String departingStation, String arrivingStation, String departingDate, String departingTime) {
        //todo: check if whitespace is at end of string before continuing

        // check if all fields are filled
        if(departingStation.isEmpty()) {
            mDepartureActv.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(arrivingStation.isEmpty()) {
            mArrivalActv.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(departingDate.isEmpty()) {
            mDateEt.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(departingTime.isEmpty()) {
            mTimeEt.setError(getString(R.string.error_form_completion));
            return false;
        }

        // check if origin and destination are different
        if (departingStation.equals(arrivingStation)) {
            mArrivalActv.setError(getString(R.string.error_form_completion2));
            return false;
        }

        //check if both stations are matching in strings resource
        if(!doesStationExist(departingStation)) {
            mDepartureActv.setError(getString(R.string.error_station_not_found));
            return false;
        }

        if(!doesStationExist(arrivingStation)) {
            mArrivalActv.setError(getString(R.string.error_station_not_found));
            return false;
        }
        return true;
    }

    private void attemptTripSearch(String origin, String destination, String date, String time) {
        if(validateUserInput(origin, destination, date, time)) {
            launchTripResultFragment(origin, destination, date, time);
        }
    }

    private void launchTripResultFragment(String origin, String destination, String date, String time) {
        Bundle bundle = initBundler(origin, destination, date, time);
        initFragmentManager(bundle);
    }

    private Bundle initBundler(String origin, String destination, String date, String time) {
        Bundle bundle = new Bundle();
        bundle.putString(TripBundle.ORIGIN.getValue(), origin);
        bundle.putString(TripBundle.DESTINATION.getValue(), destination);
        bundle.putString(TripBundle.DATE.getValue(), date);
        bundle.putString(TripBundle.TIME.getValue(), time);
        return bundle;
    }

    private void initFragmentManager(Bundle bundle) {
        Fragment frag = new BartResultsFragment();
        frag.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft;
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContent, frag).addToBackStack(null).commit();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleTrainHeaders() {
                /*
                        mTrainHeaders = new ArrayList<>();

                for(FullTrip fullTrip : mFullTripList) {
                    mTrainHeaders.add(fullTrip.getLegList().get(0).getTrainHeadStation());
                    if(DebugController.DEBUG) Log.d("trainHeader added", fullTrip.getLegList().get(0).getTrainHeadStation());
                }
                bundle.putStringArrayList(TripFragment.TripBundle.TRAIN_HEADERS.getValue(), (ArrayList<String>) mTrainHeaders);
         */
    }

    public enum TripBundle {
        ORIGIN("Origin"), DESTINATION("Destination"), DATE("DATE"), TIME("TIME"), TRAIN_HEADERS("TrainHeaders");
        private String value;

        TripBundle(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }
}
