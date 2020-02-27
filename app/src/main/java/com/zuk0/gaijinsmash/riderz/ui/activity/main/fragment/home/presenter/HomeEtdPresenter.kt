package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.databinding.PresenterEstimateBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.CustomSpinnerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EstimateRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EtdRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class HomeEtdPresenter(val context: Context?, val etdBinding: PresenterEstimateBinding, val viewModel: HomeViewModel) : LifecycleObserver, CoroutineScope by MainScope(){

    private lateinit var localEtdAdapter: EstimateRecyclerAdapter
    private lateinit var favoriteEtdAdapter: EstimateRecyclerAdapter

    init {
        Log.d(TAG, "init")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG,"onCreate")
        initSpinner()
        initSwipeRefreshLayout()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG,"onStart")

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG,"onResume")

        //restore state if available - from viewmodel
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG,"onPause")

        //save to viewmodel and push to savedInstanceState for caching.
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG,"onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d(TAG,"onDestroy")

        /*
         if (mEtdAdapter != null && mEtdInverseAdapter != null) {
             mEtdAdapter!!.destroyTimers()
             mEtdInverseAdapter!!.destroyTimers()
         }
         */
    }

    private fun initSwipeRefreshLayout() {
        etdBinding.homeSwipeRefreshLayout.setOnRefreshListener {
            refreshLists()
        }
    }

    //TODO - automatic refresh cycles every 5 minutes - do not track all trips.
    private fun refreshLists() {
        //todo refresh lists with diffutil
        initEstimateLists()
    }

    private fun initSpinner() {
        launch {
            val stationsTask = withContext(Dispatchers.IO) {
                viewModel.getStationsFromDb()
            }

            val adapter2 = CustomSpinnerAdapter(context!!, R.layout.custom_dropdown_item2, stationsTask)

            etdBinding.etdSpinner.adapter = adapter2

            etdBinding.etdSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "onNothingSelected()")
                    viewModel.selectedStation?.let {
                        etdBinding.etdStationButton.text = viewModel.selectedStation?.name
                    }
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d(TAG, "onItemSelected()")
                    etdBinding.homeSwipeRefreshLayout.isRefreshing = true
                    viewModel.selectedStation = etdBinding.etdSpinner.selectedItem as Station
                    viewModel.selectedStation?.let {
                        etdBinding.etdStationButton.text = viewModel.selectedStation?.name
                        refreshLists()
                    }
                }
            }
            etdBinding.etdStationButton.text = viewModel.selectedStation?.name
            initStation() //start here to prevent potential NPEs from race conditions
        }
    }

    private fun initStation() {
        viewModel.getNearestStation(viewModel.getLocation()).observeForever {
            etdBinding.etdStationButton.text = it.name
            viewModel.selectedStation = it
            val selectedPosition = (etdBinding.etdSpinner.adapter as CustomSpinnerAdapter).getPosition(it)
            etdBinding.etdSpinner.setSelection(selectedPosition)
            refreshLists()
        }
        etdBinding.etdStationButton.setOnClickListener { v ->
            etdBinding.etdSpinner.performClick()
        }
    }

    private fun initEstimateLists() {
        //get station from spinner - default is walnut creek
        if(viewModel.selectedStation == null)
            etdBinding.homeSwipeRefreshLayout.isRefreshing = false

        viewModel.selectedStation?.let { station ->
            viewModel.getEstimatesLiveData(station).observeForever { data ->

                when(data.status) {
                    LiveDataWrapper.Status.SUCCESS -> {
                        if(data.data.station?.etdList != null) {
                            viewModel.createEstimateListsByPlatform(data.data.station?.etdList)

                            //platform1
                            etdBinding.homeEtdPlatform1Title.text = etdBinding.root.context.resources.getString(R.string.platform) + " 1"
                            etdBinding.homeEtdPlatform1Rv.visibility = View.VISIBLE
                            etdBinding.homeEtdPlatform1Rv.setHasFixedSize(true)
                            //etdBinding.homeEtdPlatform1Rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                            etdBinding.homeEtdPlatform1Rv.layoutManager = LinearLayoutManager(etdBinding.etdContainer.context)
                            etdBinding.homeEtdPlatform1Rv.adapter = EtdRecyclerAdapter(viewModel.platform1)

                            //platform2
                            etdBinding.homeEtdPlatform2Title.text = etdBinding.root.context.resources.getString(R.string.platform)  + " 2"
                            etdBinding.homeEtdPlatform2Rv.visibility = View.VISIBLE
                            etdBinding.homeEtdPlatform2Rv.setHasFixedSize(true)
                            //etdBinding.homeEtdPlatform2Rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                            etdBinding.homeEtdPlatform2Rv.layoutManager = LinearLayoutManager(etdBinding.etdContainer.context)
                            etdBinding.homeEtdPlatform2Rv.adapter = EtdRecyclerAdapter(viewModel.platform2)

                        } else if (TimeDateUtils.isAfterHours) {
                            showAfterHoursLayout()
                        }
                        etdBinding.homeSwipeRefreshLayout.isRefreshing = false
                    }
                    LiveDataWrapper.Status.ERROR -> {
                        data.msg?.let { Logger.e(it) }
                        etdBinding.homeSwipeRefreshLayout.isRefreshing = false
                        showErrorLayout()
                    }
                    LiveDataWrapper.Status.LOADING -> {
                        Logger.i("loading")
                    }
                    else -> Log.wtf(TAG, "unknown state")
                }
            }
        }

        // if favorites is available, check if favorite station IS nearbyStation
        // if they are the same don't show,
        // if they are different, then show favorite  etd list
        // if location is OFF, but favorites is available then show both directions

    }


    /**
     * Create a favorite object to handle the return trip
     * train headers must be in ABBR format

    private fun loadInverseTripData(favorite: Favorite) {
        viewModel?.getTripLiveData(favorite.b?.abbr!!, favorite.a?.abbr!!).observe(this, Observer { response ->
            val status = response.status
            if(status == LiveDataWrapper.Status.SUCCESS) {
                if(response.data.root.schedule.request.tripList != null) {
                    val trips: List<Trip> = response.data.root.schedule.request.tripList
                    val inverse = viewModel?.createFavoriteInverse(trips, favorite)
                    //displayInverseEtd(inverse)
                }
            }

            if(status == LiveDataWrapper.Status.ERROR) {
                //updateProgressBar()
            }
        })
    }*/

    private fun initFavoriteObserver() {
        viewModel.maybeFavorite
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: DisposableMaybeObserver<Favorite>() {
                    override fun onSuccess(fav: Favorite) {
                        viewModel.isFavoriteAvailable = true
                        //loadTripData(fav)
                        //loadInverseTripData(fav)
                    }

                    override fun onComplete() {
                        //loadCallToAction(viewModel.isFavoriteAvailable)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e(e.toString())
                    }
                })
    }

    /*
       Fetches an ETD list for the user's favorite.priority route
       WARNING: TRAIN HEADERS should be in ABBREVIATED FORMAT
    */
    /*
    private fun loadTripData(favorite: Favorite) {
        if(favorite.a != null && favorite.b != null) {
            viewModel?.getTripLiveData(favorite.a?.abbr, favorite.b?.abbr)?.observe(this, Observer { response ->
                val trips: List<Trip>
                val status = response.status

                if(status == LiveDataWrapper.Status.SUCCESS) {
                    if(response.data.root.schedule.request.tripList != null) {
                        trips = response.data.root.schedule.request.tripList
                        viewModel?.setTrainHeaders(trips, favorite)
                        displayFavoriteEtd(favorite)
                    }
                }

                if(status == LiveDataWrapper.Status.ERROR) {
                    //updateProgressBar()
                }
            })
        }
    }


 */
    /*
    private fun displayFavoriteEtd(favorite: Favorite) {
        viewModel.getEtdLiveData(favorite.a?.abbr!!).observe(this, Observer { data ->
            when(data.status) {
                LiveDataWrapper.Status.SUCCESS -> {
                    if (data != null) {
                        viewModel.mFavoriteEstimateList = viewModel.getEstimatesFromEtd(favorite, data.data.station.etdList)
                        favoriteEtdAdapter = EstimateRecyclerAdapter(viewModel.mFavoriteEstimateList)
                        binding.homeEtdRecyclerView2.visibility = View.VISIBLE
                        binding.homeEtdRecyclerView2.adapter = favoriteEtdAdapter
                        binding.homeEtdRecyclerView2.layoutManager = LinearLayoutManager(binding.root.context)
                    }
                }
                LiveDataWrapper.Status.ERROR -> {
                    //todo
                }
                else  -> {}
            }
        })
    }


        Fetches an ETD for the opposite direction of the favorite

    private fun displayInverseEtd(inverse: Favorite) {
        viewModel.getEtdLiveData(inverse.a?.abbr!!).observe(this, Observer { data ->
            if(data.status == LiveDataWrapper.Status.SUCCESS) {
                if (data != null) {
                    viewModel.mInverseEstimateList = viewModel.getEstimatesFromEtd(inverse, data.data.station.etdList)
                    mEtdInverseAdapter = EstimateRecyclerAdapter(viewModel.mInverseEstimateList)
                    binding.homeEtdRecyclerView2.visibility = View.VISIBLE
                    binding.homeEtdRecyclerView2.adapter = mEtdInverseAdapter
                    binding.homeEtdRecyclerView2.layoutManager = LinearLayoutManager(activity)
                }
            }
            if(data.status == LiveDataWrapper.Status.ERROR) {
                //todo:
            }

            //updateProgressBar()
        })
    } */

    private fun showAfterHoursLayout() {
        //if between 0100 and 0600 and etd == null or empty
        etdBinding.homeSwipeRefreshLayout.visibility = View.GONE
        etdBinding.afterHoursLayout.visibility = View.VISIBLE
    }

    private fun showErrorLayout() {
        etdBinding.homeSwipeRefreshLayout.visibility = View.GONE
        etdBinding.errorLayout.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "HomeEtdPresenter"
    }
}