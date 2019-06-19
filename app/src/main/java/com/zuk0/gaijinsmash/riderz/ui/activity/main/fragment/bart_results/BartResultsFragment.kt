package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.app.AlertDialog
import androidx.fragment.app.Fragment
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
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper

import javax.inject.Inject

import androidx.recyclerview.widget.LinearLayoutManager
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import dagger.android.support.AndroidSupportInjection

class BartResultsFragment : BaseFragment() {

    @Inject
    internal var mBartResultsViewModelFactory: BartResultsViewModelFactory? = null

    private lateinit var mDataBinding: FragmentResultsBinding
    private lateinit var viewModel: BartResultsViewModel
    private var mOrigin: String? = null
    private var mDestination: String? = null
    private var mDate: String? = null
    private var mTime: String? = null
    private var mFromRecyclerAdapter = false

    private var mFavoriteIcon: MenuItem? = null
    private var mFavoritedIcon: MenuItem? = null
    private var mFavoriteObject: Favorite? = null
    private var mTripList: List<Trip>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)
        return mDataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initBundleFromTripFragment()
        if (mFromRecyclerAdapter) {
            initTripCall(mOrigin, mDestination, mDate, mTime)
        } else {
            initStationsForTripCall(mOrigin, mDestination)
        }
        initFavoriteIcon(mOrigin, mDestination)
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
                addFavorite(mFavoriteObject)
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
        viewModel = ViewModelProviders.of(this, mBartResultsViewModelFactory).get(BartResultsViewModel::class.java)
    }

    private fun initBundleFromTripFragment() { //TODO refactor
        val bundle = arguments
        if (bundle != null) {
            mOrigin = bundle.getString(TripFragment.TripBundle.ORIGIN.value)
            mDestination = bundle.getString(TripFragment.TripBundle.DESTINATION.value)
            mDate = bundle.getString(TripFragment.TripBundle.DATE.value)
            mTime = bundle.getString(TripFragment.TripBundle.TIME.value)
            mFromRecyclerAdapter = bundle.getBoolean("FAVORITE_RECYCLER_ADAPTER")
        }
    }

    //TODO
    private fun initStationsForTripCall(origin: String?, destination: String?) {
        val liveData = viewModel.getStationsFromDb(activity, origin, destination)
        liveData.observe(this, Observer { data ->
            val depart: String
            val arrive: String
            if (data != null && data!!.size > 0) {
                if (data!!.get(0).getName() == origin) {
                    depart = data!!.get(0).getAbbr()
                    arrive = data!!.get(1).getAbbr()
                } else {
                    depart = data!!.get(1).getAbbr()
                    arrive = data!!.get(0).getAbbr()
                }
                initTripCall(depart, arrive, mDate, mTime)
            } else {
                Log.wtf("initLiveData for Trip", "NULL")
            }
        })
    }

    private fun initTripCall(originAbbr: String?, destAbbr: String?, date: String?, time: String?) {
        viewModel!!.getTrip(originAbbr, destAbbr, date, time)
                .observe(this, Observer { response ->
                    val status = response.getStatus()
                    if (status == LiveDataWrapper.Status.SUCCESS) {
                        mTripList = response.getData().getRoot().getSchedule().getRequest().getTripList()
                        initFavoriteObject(mOrigin, mDestination, mTripList)
                        initRecyclerView(mTripList)
                    }

                    if (status == LiveDataWrapper.Status.ERROR) {
                        Log.wtf("initTripCall", response.getMsg())
                    }
                })
    }

    private fun initRecyclerView(tripList: List<Trip>?) {
        if (tripList != null) {
            val adapter = TripRecyclerAdapter2(tripList)
            mDataBinding!!.resultsRecyclerView.adapter = adapter
            mDataBinding!!.resultsRecyclerView.layoutManager = LinearLayoutManager(activity)
            mDataBinding!!.bartResultsProgressBar.visibility = View.GONE
        }
    }

    private fun initFavoriteObject(origin: String?, destination: String?, tripList: List<Trip>?) {
        mFavoriteObject = viewModel!!.createFavorite(origin, destination, tripList)
    }

    private fun initFavoriteIcon(origin: String?, destination: String?) {
        val org: String?
        val dest: String?

        if (mFromRecyclerAdapter) {
            //convert to full name
            org = StationList.stationMap[origin!!.toUpperCase()]
            dest = StationList.stationMap[destination!!.toUpperCase()]
        } else {
            org = origin
            dest = destination
        }

        viewModel.getFavoriteLiveData(org, dest).observe(this, Observer { data ->
            if (data != null) {
                // Current trip is already a Favorite
                mFavoritedIcon!!.isVisible = true
                mFavoriteIcon!!.setVisible(false)
            } else {
                mFavoritedIcon!!.isVisible = false
                mFavoriteIcon!!.setVisible(true)
            }
        })
    }

    private fun addFavorite(favorite: Favorite?) {
        viewModel!!.handleFavoritesIcon(RiderzEnums.FavoritesAction.ADD_FAVORITE, favorite)
        Toast.makeText(activity, resources.getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
    }

    private fun removeFavorite() {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle(resources.getString(R.string.alert_dialog_remove_favorite_title))
        alertDialog.setMessage(resources.getString(R.string.alert_dialog_confirmation))
        alertDialog.setPositiveButton(resources.getString(R.string.alert_dialog_yes)
        ) { dialog, which ->
            viewModel!!.handleFavoritesIcon(RiderzEnums.FavoritesAction.DELETE_FAVORITE, mFavoriteObject)
            mFavoritedIcon!!.isVisible = false
            mFavoriteIcon!!.isVisible = true
        }
        alertDialog.setNegativeButton(resources.getString(R.string.alert_dialog_no)) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }
}
