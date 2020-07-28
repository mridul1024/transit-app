package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentTripBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

import androidx.navigation.fragment.NavHostFragment
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.utils.KeyboardUtils

class TripFragment : BaseFragment() {

    private lateinit var mDataBinding: FragmentTripBinding
    private lateinit var mViewModel: TripViewModel
    private var mTimePickerDialog: TimePickerDialog? = null
    private var mDatePickerDialog: DatePickerDialog? = null
    private var mSimpleDateFormat: SimpleDateFormat? = null
    private var mIs24HrTimeOn = false
    private var mSpinnerAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        return mDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        super.collapseAppBar(activity)
        super.setTitle(activity, getString(R.string.trip_title))
        initTimePreference(activity)
        initStationsList()
        initSpinnerAdapter()
        initKeyboardSettings()
        initDatePicker()
        initTimePicker()
        initTextInputEditors()
        initSearchButton()
        initDepartingSpinner(mSpinnerAdapter)
        initArrivingSpinner(mSpinnerAdapter)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(TripViewModel::class.java)
    }

    private fun initTimePreference(context: Context?) {
        mIs24HrTimeOn = SharedPreferencesUtils.getTimePreference(context)
    }

    private fun initStationsList() {
        // This data is to provide a station list for Spinner and AutoCompleteTextView
        mViewModel.stationsList = resources.getStringArray(R.array.stations_list)
    }

    private fun initSpinnerAdapter() {
        mSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.custom_dropdown_item, mViewModel.stationsList)
        mSpinnerAdapter?.setDropDownViewResource(R.layout.custom_dropdown_item)
    }

    private fun initKeyboardSettings () {
        mDataBinding.tripDepartureAutoCompleteTextView.setImeActionLabel(resources.getString(R.string.button_next), KeyEvent.KEYCODE_ENTER)
        mDataBinding.tripDepartureAutoCompleteTextView.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    mDataBinding.tripArrivalAutoCompleteTextView.requestFocus()
                    KeyboardUtils.openKeyboard(context)
                    return true
                }
                return false
            }
        })
        mDataBinding.tripArrivalAutoCompleteTextView.setImeActionLabel(resources.getString(R.string.button_next), KeyEvent.KEYCODE_ENTER)
        mDataBinding.tripArrivalAutoCompleteTextView.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    mDataBinding.tripDateEditText.requestFocus()
                    return true
                }
                return false
            }
        })
        mDataBinding.tripDateEditText.setImeActionLabel(resources.getString(R.string.button_next), KeyEvent.KEYCODE_ENTER)
        mDataBinding.tripDateEditText.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    mDataBinding.tripTimeEditText.requestFocus()
                    return true
                }
                return false
            }
        })
        mDataBinding.tripTimeEditText.setImeActionLabel(resources.getString(R.string.button_search), KeyEvent.KEYCODE_ENTER)
        mDataBinding.tripTimeEditText.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    getUserInputFields()
                    attemptTripSearch(mViewModel.originField.trim(), mViewModel.destinationField.trim(), mViewModel.dateField, mViewModel.timeField)
                    return true
                }
                return false
            }
        })
    }

    private fun initDepartingSpinner(adapter: ArrayAdapter<String>?) {
        mDataBinding.tripDepartureAutoCompleteTextView.setDropDownBackgroundResource(R.color.primaryDarkColor)
        mDataBinding.tripDepartureAutoCompleteTextView.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                if(mDataBinding.tripDepartureAutoCompleteTextView.text.isNullOrBlank()) {
                    mDataBinding.tripDepartCloseBtn.visibility =  View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
        }
        mDataBinding.tripDepartCloseBtn.setOnClickListener { v ->
            if(v.visibility == View.VISIBLE) {
                v.visibility = View.INVISIBLE
                mDataBinding.tripDepartureAutoCompleteTextView.text.clear()
            }
        }
        mDataBinding.stationSpinner1.adapter = adapter
        mDataBinding.stationSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Logger.i("position $position")
                mDataBinding.stationSpinner1.selectedView?.let {
                    val textView = mDataBinding.stationSpinner1.selectedView as TextView
                    var itemSelected = ""
                    itemSelected = textView.text.toString()
                    mDataBinding.tripDepartureAutoCompleteTextView.setText(itemSelected)
                    if(itemSelected.isNotBlank())
                        mDataBinding.tripDepartCloseBtn.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                Logger.e("todo")
            }
        }
    }

    private fun showCloseButton(isDeparting: Boolean) {
        if(isDeparting) {
            mDataBinding.tripDepartCloseBtn.visibility = View.VISIBLE
        } else {
            mDataBinding.tripArrivalCloseBtn.visibility = View.VISIBLE
        }
    }

    private fun initArrivingSpinner(adapter: ArrayAdapter<String>?) {
        mDataBinding.tripArrivalAutoCompleteTextView.setDropDownBackgroundResource(R.color.primaryDarkColor)
        mDataBinding.tripArrivalAutoCompleteTextView.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                if(mDataBinding.tripArrivalAutoCompleteTextView.text.isNullOrBlank()) {
                    mDataBinding.tripArrivalCloseBtn.visibility =  View.INVISIBLE
                } else {
                    showCloseButton(true)
                }
            }
        }
        mDataBinding.tripArrivalCloseBtn.setOnClickListener { v ->
            v?.let {
                if(v.visibility == View.VISIBLE) {
                    v.visibility = View.INVISIBLE
                    mDataBinding.tripArrivalAutoCompleteTextView.text.clear()
                } else
                    v.visibility = View.VISIBLE
            }
        }

        mDataBinding.stationSpinner2.adapter = adapter
        mDataBinding.stationSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mDataBinding.stationSpinner2.selectedView?.let {
                    val textView = mDataBinding.stationSpinner2.selectedView as TextView
                    var itemSelected = ""
                    itemSelected = textView.text?.toString() ?: ""
                    mDataBinding.tripArrivalAutoCompleteTextView.setText(itemSelected)
                    if(itemSelected.isNotBlank())
                        mDataBinding.tripArrivalCloseBtn.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                Logger.e("todo")
            }
        }
    }

    private fun initDatePicker() {
        mDataBinding.tripDateEditText.setText(getText(R.string.currentDate))
        mDataBinding.tripDateEditText.inputType = InputType.TYPE_NULL
        mDataBinding.tripDateEditText.requestFocus()
        mDataBinding.tripDateEditText.setOnClickListener { _ ->
            val newCalendar = Calendar.getInstance()
            context?.let {
                mDatePickerDialog = DatePickerDialog(it, { _, year, month, day ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, month, day)
                    mSimpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                    mDataBinding.tripDateEditText.setText(mSimpleDateFormat!!.format(newDate.time))
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
                mDatePickerDialog?.show()
            }
        }
    }

    private fun initTimePicker() {
        mDataBinding.tripTimeEditText.setText(getText(R.string.currentTime))
        mDataBinding.tripTimeEditText.inputType = InputType.TYPE_NULL
        mDataBinding.tripTimeEditText.requestFocus()
        mDataBinding.tripTimeEditText.setOnClickListener { view12 ->
            // Current time is default value
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            // Create and return a new instance of TimePickerDialog
            mTimePickerDialog = TimePickerDialog(activity, { view121, selectedHour, selectedMinute ->
                // Returned value is always 24hr so conversion is necessary
                if (mIs24HrTimeOn) {
                    val formattedTime = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)
                    mDataBinding.tripTimeEditText.setText(formattedTime)
                } else {
                    val convertedTime = TimeDateUtils.convertTo12HrForTrip("$selectedHour:$selectedMinute")
                    mDataBinding.tripTimeEditText.setText(convertedTime)
                }
            }, hour, minute, mIs24HrTimeOn) //True = 24 hour format on TimePicker only
            mTimePickerDialog?.setTitle(getString(R.string.time_title))
            mTimePickerDialog?.show()
        }
    }

    private fun initTextInputEditors() {
        val textViewAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_selectable_list_item, mViewModel.stationsList)
        mDataBinding.tripDepartureAutoCompleteTextView.threshold = 1 // will start working from first character
        mDataBinding.tripDepartureAutoCompleteTextView.setAdapter(textViewAdapter)
        mDataBinding.tripDepartureAutoCompleteTextView.requestFocus()

        mDataBinding.tripArrivalAutoCompleteTextView.threshold = 1
        mDataBinding.tripArrivalAutoCompleteTextView.setAdapter(textViewAdapter)
    }

    private fun initSearchButton() {
        mDataBinding.tripButton.setOnClickListener { view13 ->
            getUserInputFields()
            attemptTripSearch(mViewModel.originField.trim(), mViewModel.destinationField.trim(), mViewModel.dateField, mViewModel.timeField)
        }
    }

    private fun getUserInputFields() {
        /*
            NOTE: stations have not been formatted to their abbreviated versions yet.
            From the BartResultsFragment, take String values from Bundle
            and convert them appropriately before making API call
        */
        mViewModel.originField = mDataBinding.tripDepartureAutoCompleteTextView.text.toString()
        mViewModel.destinationField = mDataBinding.tripArrivalAutoCompleteTextView.text.toString()

        // DATE
        mViewModel.dateField = Objects.requireNonNull<Editable>(mDataBinding.tripDateEditText.text).toString()

        // TIME
        val preformatTime = Objects.requireNonNull<Editable>(mDataBinding.tripTimeEditText.text).toString()
        mViewModel.timeField = mViewModel.getTimeForTripSearch(preformatTime, mIs24HrTimeOn)
    }

    private fun validateUserInput(departingStation: String, arrivingStation: String, departingDate: String, departingTime: String): Boolean {
        // check if all fields are filled
        if (departingStation.isBlank()) {
            mDataBinding.tripDepartureAutoCompleteTextView.error = getString(R.string.error_form_completion)
            return false
        }
        if (arrivingStation.isEmpty()) {
            mDataBinding.tripArrivalAutoCompleteTextView.error = getString(R.string.error_form_completion)
            return false
        }
        if (departingDate.isEmpty()) {
            mDataBinding.tripDateEditText.error = getString(R.string.error_form_completion)
            return false
        }
        if (departingTime.isEmpty()) {
            mDataBinding.tripTimeEditText.error = getString(R.string.error_form_completion)
            return false
        }

        // check if origin and destination are different
        if (departingStation == arrivingStation) {
            mDataBinding.tripArrivalAutoCompleteTextView.error = getString(R.string.error_form_completion2)
            return false
        }

        //check if both stations are matching in strings resource
        if (!mViewModel.doesStationExist(departingStation, context)) {
            mDataBinding.tripDepartureAutoCompleteTextView.error = getString(R.string.error_station_not_found)
            return false
        }

        if (!mViewModel.doesStationExist(arrivingStation, context)) {
            mDataBinding.tripArrivalAutoCompleteTextView.error = getString(R.string.error_station_not_found)
            return false
        }
        return true
    }

    private fun attemptTripSearch(origin: String, destination: String, date: String, time: String) {
        if (validateUserInput(origin, destination, date, time)) {
            launchTripResultFragment(origin, destination, date, time)
        }
    }

    private fun launchTripResultFragment(origin: String, destination: String, date: String, time: String) {
        val bundle = initBundler(origin, destination, date, time)
        NavHostFragment.findNavController(this).navigate(R.id.action_tripFragment_to_resultsFragment, bundle, null, null)
    }

    private fun initBundler(origin: String, destination: String, date: String, time: String): Bundle { //TODO
        val bundle = Bundle()
        bundle.putString(TripBundle.ORIGIN.value, origin)
        bundle.putString(TripBundle.DESTINATION.value, destination)
        bundle.putString(TripBundle.DATE.value, date)
        bundle.putString(TripBundle.TIME.value, time)
        return bundle
    }

    enum class TripBundle private constructor(val value: String) { //TODO
        ORIGIN("Origin"), DESTINATION("Destination"), DATE("DATE"), TIME("TIME"), TRAIN_HEADERS("TrainHeaders")
    }

}
