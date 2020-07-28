package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.databinding.FragmentStationInfoBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment

import javax.inject.Inject

import androidx.navigation.fragment.NavHostFragment

class StationInfoFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var dataBinding: FragmentStationInfoBinding
    private lateinit var viewModel: StationInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        getBundleArgs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding = FragmentStationInfoBinding.inflate(inflater)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.stationInfoMapBtn.setOnClickListener { v -> handleMapButtonClick() }
        super.collapseAppBar(activity)
        super.setTitle(activity, getString(R.string.station_info_title))
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(StationInfoViewModel::class.java)
    }

    private fun handleMapButtonClick() {
        viewModel.mStationObject?.let {
            val bundle = viewModel.getBundle(it)
            NavHostFragment.findNavController(this).navigate(R.id.action_stationInfoFragment_to_googleMapFragment, bundle, null, null)
        }
    }

    private fun getBundleArgs() {
        val extras = arguments
        if (extras != null) {
            viewModel.mStationAbbr = extras.getString(STATION_INFO_EXTRA)
            initStationDetails()
        }
    }

    private fun initStationDetails() {
        
        viewModel.getStationLiveData(viewModel.mStationAbbr)?.observe(this, Observer { stationObject ->
            dataBinding.stationInfoProgressBar.visibility = View.GONE

            //update the ui
            var station1: Station? = null
            if (stationObject != null) {
                station1 = stationObject.stationList?.get(0)
            }
            if (station1 != null) {
                // build station object for map button
                viewModel.mStationObject = station1

                //update ui
                dataBinding.stationInfoTitleTextView.text = station1.name
                Log.i("name", station1?.name ?: "null")
                dataBinding.stationInfoAddressTextView.text = station1.address
                dataBinding.stationInfoCityTextView.text = station1.city
                dataBinding.stationInfoCrossStreetTextView.text = station1.crossStreet
                dataBinding.stationInfoLinkTextView.text = station1.link
                dataBinding.stationInfoIntroTextView.text = station1.intro

                if (Build.VERSION.SDK_INT >= 24) {
                    dataBinding.stationInfoAttractionTextView.text = Html.fromHtml(station1.attraction, Html.FROM_HTML_MODE_LEGACY)
                    dataBinding.stationInfoShoppingTextView.text = Html.fromHtml(station1.shopping, Html.FROM_HTML_MODE_LEGACY)
                    dataBinding.stationInfoFoodTextView.text = Html.fromHtml(station1.food, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    dataBinding.stationInfoAttractionTextView.text = Html.fromHtml(station1.attraction)
                    dataBinding.stationInfoShoppingTextView.text = Html.fromHtml(station1.shopping)
                    dataBinding.stationInfoFoodTextView.text = Html.fromHtml(station1.food)
                }
            } else {
                dataBinding.stationInfoTitleTextView.text = resources.getString(R.string.stationInfo_oops)
                dataBinding.stationInfoAddressTextView.visibility = View.GONE
                dataBinding.stationInfoCityTextView.visibility = View.GONE
                dataBinding.stationInfoCrossStreetTextView.visibility = View.GONE
                dataBinding.stationInfoIntroTextView.text = resources.getString(R.string.stationInfo_error)
                dataBinding.stationInfoLinkTextView.visibility = View.GONE
                dataBinding.stationInfoAttractionTextView.visibility = View.GONE
                dataBinding.stationInfoShoppingTextView.visibility = View.GONE
                dataBinding.stationInfoFoodTextView.visibility = View.GONE
            }
        })
    }

    companion object {
        const val TAG = "StationInfoFragment"
        var STATION_INFO_EXTRA = "STATION_INFO_EXTRA"
    }
}
