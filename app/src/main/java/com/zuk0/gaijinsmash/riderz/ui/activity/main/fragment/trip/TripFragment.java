package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.databinding.ViewTripBinding;
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.navigation.fragment.NavHostFragment;
import dagger.android.support.AndroidSupportInjection;

public class TripFragment extends Fragment {

    private ViewTripBinding mDataBinding;
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
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_trip, container, false);
        return mDataBinding.getRoot();
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
        mDataBinding.stationSpinner1.setAdapter(adapter);
        mDataBinding.stationSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) mDataBinding.stationSpinner1.getSelectedView();
                String itemSelected = "";
                if (textView != null) {
                    itemSelected = textView.getText().toString();
                }
                mDataBinding.tripDepartureAutoCompleteTextView.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initArrivingSpinner(ArrayAdapter<String> adapter) {
        mDataBinding.stationSpinner2.setAdapter(adapter);
        mDataBinding.stationSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) mDataBinding.stationSpinner2.getSelectedView();
                String itemSelected = "";
                if(textView != null){
                    itemSelected = textView.getText().toString();
                }
                mDataBinding.tripArrivalAutoCompleteTextView.setText(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initDatePicker() {
        mDataBinding.tripDateEditText.setText(getText(R.string.currentDate));
        mDataBinding.tripDateEditText.setInputType(InputType.TYPE_NULL);
        mDataBinding.tripDateEditText.requestFocus();
        mDataBinding.tripDateEditText.setOnClickListener(view1 -> {
            Calendar newCalendar = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (view11, year, month, day) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                mDataBinding.tripDateEditText.setText(mSimpleDateFormat.format(newDate.getTime()));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        });
    }

    private void initTimePicker() {
        mDataBinding.tripTimeEditText.setText(getText(R.string.currentTime));
        mDataBinding.tripTimeEditText.setInputType(InputType.TYPE_NULL);
        mDataBinding.tripTimeEditText.requestFocus();
        mDataBinding.tripTimeEditText.setOnClickListener(view12 -> {
            // Current time is default value
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            // Create and return a new instance of TimePickerDialog
            mTimePickerDialog = new TimePickerDialog(getActivity(), (view121, selectedHour, selectedMinute) -> {
                // Returned value is always 24hr so conversion is necessary
                if(mIs24HrTimeOn) {
                    String formattedTime = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute);
                    mDataBinding.tripTimeEditText.setText(formattedTime);
                } else {
                    String convertedTime = TimeDateUtils.convertTo12HrForTrip(selectedHour + ":" + selectedMinute);
                    mDataBinding.tripTimeEditText.setText(convertedTime);
                }
            }, hour,minute,mIs24HrTimeOn); //True = 24 hour format on TimePicker only
            mTimePickerDialog.setTitle(getString(R.string.time_title));
            mTimePickerDialog.show();
        });
    }

    private void initTextInputEditors() {
        ArrayAdapter<String> textViewAdapter = new ArrayAdapter<> (Objects.requireNonNull(getActivity()), android.R.layout.simple_selectable_list_item, mStationsList);

        mDataBinding.tripDepartureAutoCompleteTextView.setThreshold(1); // will start working from first character
        mDataBinding.tripDepartureAutoCompleteTextView.setAdapter(textViewAdapter);

        mDataBinding.tripArrivalAutoCompleteTextView.setThreshold(1);
        mDataBinding.tripArrivalAutoCompleteTextView.setAdapter(textViewAdapter);
    }

    private void initSearchButton() {
        mDataBinding.tripButton.setOnClickListener(view13 -> {

            /*
                NOTE: stations have not been formatted to their abbreviated versions yet.
                From the BartResultsFragment, take String values from Bundle
                and convert them appropriately before making API call
             */
            String departingStation = mDataBinding.tripDepartureAutoCompleteTextView.getText().toString();
            String arrivingStation = mDataBinding.tripArrivalAutoCompleteTextView.getText().toString();

            // TIME
            String preformatTime = Objects.requireNonNull(mDataBinding.tripTimeEditText.getText()).toString();
            String departingTime = mViewModel.getTimeForTripSearch(preformatTime, mIs24HrTimeOn);

            // DATE
            String departingDate = Objects.requireNonNull(mDataBinding.tripDateEditText.getText()).toString();

            attemptTripSearch(departingStation.trim(), arrivingStation.trim(), departingDate, departingTime);
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
        // check if all fields are filled
        if(departingStation.isEmpty()) {
            mDataBinding.tripDepartureAutoCompleteTextView.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(arrivingStation.isEmpty()) {
            mDataBinding.tripArrivalAutoCompleteTextView.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(departingDate.isEmpty()) {
            mDataBinding.tripDateEditText.setError(getString(R.string.error_form_completion));
            return false;
        }
        if(departingTime.isEmpty()) {
            mDataBinding.tripTimeEditText.setError(getString(R.string.error_form_completion));
            return false;
        }

        // check if origin and destination are different
        if (departingStation.equals(arrivingStation)) {
            mDataBinding.tripArrivalAutoCompleteTextView.setError(getString(R.string.error_form_completion2));
            return false;
        }

        //check if both stations are matching in strings resource
        if(!doesStationExist(departingStation)) {
            mDataBinding.tripDepartureAutoCompleteTextView.setError(getString(R.string.error_station_not_found));
            return false;
        }

        if(!doesStationExist(arrivingStation)) {
            mDataBinding.tripArrivalAutoCompleteTextView.setError(getString(R.string.error_station_not_found));
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
        NavHostFragment.findNavController(this).navigate(R.id.action_tripFragment_to_resultsFragment, bundle, null, null);
    }

    private Bundle initBundler(String origin, String destination, String date, String time) {
        Bundle bundle = new Bundle();
        bundle.putString(TripBundle.ORIGIN.getValue(), origin);
        bundle.putString(TripBundle.DESTINATION.getValue(), destination);
        bundle.putString(TripBundle.DATE.getValue(), date);
        bundle.putString(TripBundle.TIME.getValue(), time);
        return bundle;
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
