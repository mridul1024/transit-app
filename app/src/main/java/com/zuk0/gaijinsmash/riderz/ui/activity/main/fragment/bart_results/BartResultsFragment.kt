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
import androidx.lifecycle.ViewModelProvider

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
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment

/*
    Note: always pass
 */
class BartResultsFragment : BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentResultsBinding
    private lateinit var viewModel: BartResultsViewModel

    private var mFavoriteIcon: MenuItem? = null
    private var mFavoritedIcon: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        viewModel.handleIntentExtras(arguments)
        if(savedInstanceState != null)
            viewModel.restoreState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentResultsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStationsForTripCall(viewModel.origin, viewModel.destination, viewModel.date, viewModel.time)
        super.collapseAppBar(activity)
    }

    override fun onResume() {
        super.onResume()
        initFavoriteIcon(viewModel.originStation, viewModel.destinationStation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //todo - add caching
        super.onSaveInstanceState(outState)
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
        viewModel = ViewModelProvider(this, viewModelFactory).get(BartResultsViewModel::class.java)
    }

    /**
     * Stations must be in abbreviated format for the API call
     */
    private fun initStationsForTripCall(origin: String, destination: String, date: String, time: String) {
        Logger.i("origin: $origin, destination: $destination, date: $date, time: $time")

        viewModel.loadTrip(origin, destination, date, time).observe(viewLifecycleOwner, Observer { result ->
            when(result.status) {
                LiveDataWrapper.Status.SUCCESS -> {
                    viewModel.mTripList = result.data.root?.schedule?.request?.tripList
                    initFavoriteObject(viewModel.originStation, viewModel.destinationStation, viewModel.mTripList)
                    initRecyclerView(viewModel.mTripList)
                }
                LiveDataWrapper.Status.LOADING -> {
                    Logger.i("Loading Trip")
                }
                LiveDataWrapper.Status.ERROR -> {
                    result.msg?.let { Logger.e(it) }
                }
                else -> { Logger.wtf("unknown error") }
            }
        })
    }

    private fun initRecyclerView(tripList: List<Trip>?) {
        if (tripList != null) {
            val adapter = TripRecyclerAdapter2(tripList)
            binding.resultsRecyclerView.adapter = adapter
            binding.resultsRecyclerView.layoutManager = LinearLayoutManager(activity)
            binding.bartResultsProgressBar.visibility = View.GONE
        }
    }

    private fun initFavoriteObject(a: Station?, b: Station?, tripList: List<Trip>?) {
        if(a != null && b != null)
            viewModel.mFavoriteObject = viewModel.createFavorite(a, b, tripList)
    }

    private fun initFavoriteIcon(a: Station?, b: Station?) {
        if(a != null && b != null) {
            viewModel.getFavoriteLiveData(a, b)?.observe(this, Observer { data ->
                if (data != null) {
                    // Current trip is already a Favorite
                    mFavoritedIcon?.isVisible = true
                    mFavoriteIcon?.isVisible = false
                } else {
                    mFavoritedIcon?.isVisible = false
                    mFavoriteIcon?.isVisible = true
                }
            })
        }
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
            mFavoritedIcon?.isVisible = false
            mFavoriteIcon?.isVisible = true
        }
        alertDialog.setNegativeButton(resources.getString(R.string.alert_dialog_no)) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }
}
