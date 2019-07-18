package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.databinding.FragmentResultsBinding
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper

import javax.inject.Inject

import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment

class BartResultsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: BartResultsViewModelFactory
    private lateinit var binding: FragmentResultsBinding
    private lateinit var viewModel: BartResultsViewModel

    private var mFavoriteIcon: MenuItem? = null
    private var mFavoritedIcon: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        viewModel.handleIntentExtras(arguments)
        viewModel.restoreState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //todo consolidate
        if (viewModel.isFromRecyclerAdapter) {
            initStationsForTripCall(viewModel.origin, viewModel.destination, viewModel.date, viewModel.time)
        } else {
            initStationsForTripCall(viewModel.origin, viewModel.destination, viewModel.date, viewModel.time)
        }
        initFavoriteIcon(viewModel.origin, viewModel.destination)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favorite, menu)
        inflater.inflate(R.menu.favorited, menu)
        mFavoriteIcon = menu.findItem(R.id.action_favorite)
        mFavoritedIcon = menu.findItem(R.id.action_favorited)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                addFavorite(viewModel.mFavoriteObject)
                return true
            }
            R.id.action_favorited -> {
                removeFavorite()
                return true
            }
        }
        return false
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BartResultsViewModel::class.java)
    }

    private fun initStationsForTripCall(origin: String, destination: String, date: String, time: String) {
        Logger.i("origin: $origin, destination: $destination, date: $date, time: $time")

        viewModel.loadTrip2(origin, destination, date, time).observe(this, Observer { result ->
            result?.let {
                when(it.status) {
                    LiveDataWrapper.Status.SUCCESS -> {
                        viewModel.mTripList = it.data.root.schedule.request.tripList
                        initFavoriteObject(viewModel.origin, viewModel.destination, viewModel.mTripList)
                        initRecyclerView(viewModel.mTripList)
                    }
                    LiveDataWrapper.Status.ERROR -> {
                        Logger.wtf(it.msg)

                    }
                }
            }
        })

/*
        val liveData = viewModel.getStationsFromDb(origin, destination)
        liveData.observe(this, Observer { data ->
            val depart: String
            val arrive: String
            if (data != null && data.isNotEmpty()) {
                if (data.get(0).name == origin) {
                    depart = data.get(0).abbr
                    arrive = data.get(1).abbr
                } else {
                    depart = data.get(1).abbr
                    arrive = data.get(0).abbr
                }
                initTripCall(depart, arrive, mDate, mTime)
            } else {
                Log.wtf("initLiveData for Trip", "NULL")
            }
        }) */
    }

    private fun initRecyclerView(tripList: List<Trip>?) {
        if (tripList != null) {
            val adapter = TripRecyclerAdapter2(tripList)
            binding.resultsRecyclerView.adapter = adapter
            binding.resultsRecyclerView.layoutManager = LinearLayoutManager(activity)
            binding.bartResultsProgressBar.visibility = View.GONE
        }
    }

    private fun initFavoriteObject(origin: String?, destination: String?, tripList: List<Trip>?) {
        viewModel.mFavoriteObject = viewModel.createFavorite(origin, destination, tripList)
    }

    private fun initFavoriteIcon(origin: String?, destination: String?) {
        val org: String?
        val dest: String?

        if (viewModel.isFromRecyclerAdapter) {
            //convert to full name
            org = StationList.stationMap[origin!!.toUpperCase()]
            dest = StationList.stationMap[destination!!.toUpperCase()]
        } else {
            org = origin
            dest = destination
        }
        //todo tes
        viewModel.getFavoriteLiveData(org!!, dest!!).observe(this, Observer { data ->
            if (data != null) {
                // Current trip is already a Favorite
                mFavoritedIcon!!.isVisible = true
                mFavoriteIcon!!.isVisible = false
            } else {
                mFavoritedIcon!!.isVisible = false
                mFavoriteIcon!!.isVisible = true
            }
        })
    }

    private fun addFavorite(favorite: Favorite?) {
        favorite?.let {
            viewModel.handleFavoritesIcon(RiderzEnums.FavoritesAction.ADD_FAVORITE, favorite)
            Toast.makeText(activity, resources.getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFavorite() {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle(resources.getString(R.string.alert_dialog_remove_favorite_title))
        alertDialog.setMessage(resources.getString(R.string.alert_dialog_confirmation))
        alertDialog.setPositiveButton(resources.getString(R.string.alert_dialog_yes)
        ) { dialog, which ->
            viewModel.handleFavoritesIcon(RiderzEnums.FavoritesAction.DELETE_FAVORITE, viewModel.mFavoriteObject!!)
            mFavoritedIcon!!.isVisible = false
            mFavoriteIcon!!.isVisible = true
        }
        alertDialog.setNegativeButton(resources.getString(R.string.alert_dialog_no)) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }
}
