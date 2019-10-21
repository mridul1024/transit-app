package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.databinding.EstimateLayoutBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EstimateRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

class HomeEtdPresenter(context: Context?) : LifecycleObserver {

    var viewModel: HomeViewModel? = null

    val binding: EstimateLayoutBinding = EstimateLayoutBinding.inflate(LayoutInflater.from(context))

    private lateinit var localEtdAdapter: EstimateRecyclerAdapter
    private lateinit var favoriteEtdAdapter: EstimateRecyclerAdapter

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initSwipeRefreshLayout()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        //restore state if available
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        //saveInstanceState
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        /*
         if (mEtdAdapter != null && mEtdInverseAdapter != null) {
             mEtdAdapter!!.destroyTimers()
             mEtdInverseAdapter!!.destroyTimers()
         } */
        Log.d("onPause", "timers destroyed")
    }

    private fun initSwipeRefreshLayout() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            //initFavoriteObserver()
            //loadUpcomingNearbyTrains()
        }
    }

    fun initEstimateLists() {
        // if location is available, show nearby station etd list
        // if favorites is available, check if favorite station IS nearbyStation
        // if they are the same don't show,
        // if they are different, then show favorite  etd list
        // if location is OFF, but favorites is available then show both directions
    }


    /*
    private fun loadUpcomingNearbyTrains() {
        viewModel?.getNearestStation(viewModel?.userLocation)?.observe(this, Observer {station ->
            station?.let {
                viewModel?.getLocalEtd(station)?.observe(this, Observer {data ->
                    when(data.status) {
                        LiveDataWrapper.Status.SUCCESS -> {
                            binding.homeEtdRecyclerView3.visibility = View.VISIBLE
                            binding.homeEtdRecyclerView3.layoutManager = LinearLayoutManager(binding.root.context)
                            viewModel?.upcomingNearbyEstimateList = viewModel?.getEstimatesFromEtd(data.data.station)
                            localEtdAdapter = EstimateRecyclerAdapter(viewModel?.upcomingNearbyEstimateList)
                            binding.homeEtdRecyclerView3.adapter = localEtdAdapter
                            binding.homeSwipeRefreshLayout.isRefreshing = false
                        }
                        LiveDataWrapper.Status.ERROR -> {
                            Logger.e(data.msg)
                            binding.homeSwipeRefreshLayout.isRefreshing = false
                        }
                        LiveDataWrapper.Status.LOADING -> {
                            Logger.i("loading")
                        }
                        else -> {

                        }
                    }
                })
            }
        })
    }
    */

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
        viewModel?.maybeFavorite
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object: DisposableMaybeObserver<Favorite>() {
                    override fun onSuccess(fav: Favorite) {
                        viewModel?.isFavoriteAvailable = true
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

}