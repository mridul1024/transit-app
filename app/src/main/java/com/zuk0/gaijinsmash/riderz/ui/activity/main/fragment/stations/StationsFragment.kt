package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentStationsBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragment

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragment.Companion.STATION_INFO_EXTRA

class StationsFragment : Fragment() {

    @Inject lateinit var mStationsViewModelFactory: ViewModelProvider.Factory

    private lateinit var mDataBinding: FragmentStationsBinding
    private lateinit var viewModel: StationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stations, container, false)
        return mDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDagger()
        initViewModel()
        initStationList()
    }

    private fun initDagger() {
        AndroidSupportInjection.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, mStationsViewModelFactory).get(StationsViewModel::class.java)
    }

    private fun handleListItemClick(view: View) {
        val bundle = initBundle(view)
        launchStationInfoFragment(bundle)
    }

    private fun initBundle(view: View): Bundle {
        val stationAbbr = (view.findViewById<View>(R.id.stationAbbr_textView) as TextView).text.toString()
        val bundle = Bundle()
        bundle.putString(StationInfoFragment.STATION_INFO_EXTRA, stationAbbr) //todo: convert StationAbbr to enum
        return bundle
    }

    private fun launchStationInfoFragment(bundle: Bundle) {
        NavHostFragment.findNavController(this).navigate(
                R.id.action_stationsFragment_to_stationInfoFragment,
                bundle, null, null
        )
    }

    private fun initStationList() {
        viewModel.getListFromDb(activity)?.observe(this, Observer { stations ->
            //update the ui
            if (stations != null) {
                val adapter = StationRecyclerAdapter(stations)
                mDataBinding.stationRecyclerView.adapter = adapter
                mDataBinding.stationRecyclerView.layoutManager = LinearLayoutManager(activity)
                adapter.setClickListener(View.OnClickListener {
                    handleListItemClick(it)
                })
            } else {
                viewModel.listFromRepo
                        .observe(this, Observer { data ->
                            val adapter: StationRecyclerAdapter
                            if (data != null) {
                                adapter = StationRecyclerAdapter(data.stationList)
                                mDataBinding.stationRecyclerView.adapter = adapter
                                mDataBinding.stationRecyclerView.layoutManager = LinearLayoutManager(activity)
                                adapter.setClickListener(View.OnClickListener {
                                    handleListItemClick(it)
                                })                            } else {
                                Log.wtf("StationsFragment", "error with list")
                            }
                        })
            }
            mDataBinding.stationProgressBar.visibility = View.GONE
        })
    }
}
