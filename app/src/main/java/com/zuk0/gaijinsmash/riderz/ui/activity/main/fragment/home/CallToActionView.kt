package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.databinding.StubCallToActionBinding
import java.util.*

class CallToActionView: View, LifecycleObserver {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: StubCallToActionBinding
    private var spinnerAdapter: ArrayAdapter<String>
    private val stationsList: List<String>

    // state
    private var origin: String? = null
    private var destination: String? = null
    private val userSelectedData = MutableLiveData<Favorite>()

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = StubCallToActionBinding.inflate(inflater) //todo verify
        stationsList = context.resources.getStringArray(R.array.stations_list).toList()
        spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, stationsList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        initOriginSpinner(spinnerAdapter)
        initDestinationSpinner(spinnerAdapter)
        initButton()
    }

    private fun initOriginSpinner(adapter: ArrayAdapter<String>) {
        binding.callToActionOriginSpinner.adapter = adapter
        binding.callToActionOriginSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val textView = binding.callToActionOriginSpinner.selectedView as TextView
                var itemSelected = ""
                itemSelected = textView.text.toString()
                binding.callToActionOriginTv.text = itemSelected
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    private fun initDestinationSpinner(adapter: ArrayAdapter<String>) {
        binding.callToActionDestinationSpinner.adapter = adapter
        binding.callToActionDestinationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val textView = binding.callToActionDestinationSpinner.selectedView as TextView
                var itemSelected = ""
                itemSelected = textView.text.toString()
                binding.callToActionDestinationTv.text = itemSelected
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                //todo handle
            }
        }
    }

    fun initButton() {
        binding.callToActionButton.setOnClickListener { view ->
            //grab data, pass to viewmodel for handling
            val favorite = Favorite()
            favorite.origin = origin
            favorite.destination = destination
            userSelectedData.postValue(favorite)
        }
    }

    /*
        Lifecycle Callbacks
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        // initialize here
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        // save state here
        //todo may need to save externally
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        // restore state here
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        //clean up here
    }
}