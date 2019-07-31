package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.databinding.FragmentHomeBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.BsaRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EstimateRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.roundToLong

//TODO add Trip Schedule for Favorites instead of ETDS
//show ETDS for only the local station.
//todo add option to get estimates based on user location - create a commute route. preferred origin and destination.
//get user location - if user location is at a bart station other than the preferred, get etd from that location instead.
//allow user to create a HOME(s) station.
//show info based on user location to either location. or button to switch

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mViewModel: HomeViewModel

    private var mEtdAdapter: EstimateRecyclerAdapter? = null
    private var mEtdInverseAdapter: EstimateRecyclerAdapter? = null
    private var localEtdAdapter: EstimateRecyclerAdapter? = null

    private lateinit var mCallToAction: CallToActionView //stubView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadWeather()
        loadAdvisories(mViewModel.bsaLiveData)
        initFavoriteObserver()
        loadUpcomingNearbyTrains()
        initSwipeRefreshLayout()
        super.expandAppBar(activity)
        expandBottomNavView()
        displayNews(false) // use this switch to turn on/off the news manually
    }

    override fun onPause() {
        super.onPause()
        if (mEtdAdapter != null && mEtdInverseAdapter != null) {
            mEtdAdapter!!.destroyTimers()
            mEtdInverseAdapter!!.destroyTimers()
        }
        Log.d("onPause", "timers destroyed")
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel::class.java)
    }

    private fun initSwipeRefreshLayout() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                loadAdvisories(mViewModel.bsaLiveData)
                //initFavoriteObserver()
                loadUpcomingNearbyTrains()
            }
        })
    }

    private fun expandBottomNavView() { // todo why?
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        HideBottomViewOnScrollBehavior<BottomNavigationView>(activity, null).slideUp(bottomNav)
    }
    /*
       A way to provide news that cannot be fetched by the api via webview.
       CardView's visibility is GONE by default
     */
    private fun displayNews(isActive: Boolean) {
        if(isActive) {
            //todo: update
            //if(SharedPreferencesUtils.getDevUpdatePreference(activity)) AlertDialogUtils.launchNotificationDialog(activity, title, msg)
        }
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
    */
    private fun loadAdvisories(bsa: LiveData<BsaXmlResponse>) {
        bsa.observe(this, Observer { bsaXmlResponse ->
            if (bsaXmlResponse != null) {
                val bsaAdapter = BsaRecyclerAdapter(bsaXmlResponse.bsaList)
                binding.homeBsaRecyclerView.adapter = bsaAdapter
                binding.homeBsaRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        })
    }

    private fun initFavoriteObserver() {
        mViewModel.maybeFavorite
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: DisposableMaybeObserver<Favorite>() {
                    override fun onSuccess(fav: Favorite) {
                        mViewModel.isFavoriteAvailable = true
                        loadTripData(fav)
                        loadInverseTripData(fav)
                    }

                    override fun onComplete() {
                        updateProgressBar()
                        loadCallToAction(mViewModel.isFavoriteAvailable)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e(e.localizedMessage)
                        updateProgressBar()
                    }
                })
    }

    /*
       Fetches an ETD list for the user's favorite.priority route
       WARNING: TRAIN HEADERS should be in ABBREVIATED FORMAT
    */
    private fun loadTripData(favorite: Favorite) {
        mViewModel.getTripLiveData(favorite.origin, favorite.destination).observe(this, Observer { response ->
            val trips: List<Trip>
            val status = response.status

            if(status == LiveDataWrapper.Status.SUCCESS) {
                if(response.data.root.schedule.request.tripList != null) {
                    trips = response.data.root.schedule.request.tripList
                    mViewModel.setTrainHeaders(trips, favorite)
                    displayFavoriteEtd(favorite)
                }
            }

            if(status == LiveDataWrapper.Status.ERROR) {
                updateProgressBar()
            }
        })
    }

    // Create a favorite object to handle the return trip
    private fun loadInverseTripData(favorite: Favorite) {
        mViewModel.getTripLiveData(favorite.destination, favorite.origin).observe(this, Observer { response ->
            val status = response.status
            if(status == LiveDataWrapper.Status.SUCCESS) {
                if(response.data.root.schedule.request.tripList != null) {
                    val trips: List<Trip> = response.data.root.schedule.request.tripList
                    val inverse = mViewModel.createFavoriteInverse(trips, favorite)
                    displayInverseEtd(inverse)
                }
            }

            if(status == LiveDataWrapper.Status.ERROR) {
                updateProgressBar()
            }
        })
    }

    private fun loadUpcomingNearbyTrains() {

        mViewModel.getNearestStation(mViewModel.userLocation).observe(this, Observer {station ->
            station?.let {
                mViewModel.getLocalEtd(station).observe(this, Observer {data ->
                    when(data.status) {
                        LiveDataWrapper.Status.SUCCESS -> {
                            mViewModel.upcomingNearbyEstimateList = mViewModel.getEstimatesFromEtd(data.data.station)
                            localEtdAdapter = EstimateRecyclerAdapter(mViewModel.upcomingNearbyEstimateList)
                            binding.homeEtdRecyclerView3.visibility = View.VISIBLE
                            binding.homeEtdRecyclerView3.adapter = localEtdAdapter
                            binding.homeEtdRecyclerView3.layoutManager = LinearLayoutManager(activity)
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

    private fun loadCallToAction(isFavoriteAvailable: Boolean) {
        if(!isFavoriteAvailable) {
           // todo
        }
    }

    private fun displayFavoriteEtd(favorite: Favorite) {
        mViewModel.getEtdLiveData(favorite.origin).observe(this, Observer { data ->
            when(data.status) {
                LiveDataWrapper.Status.SUCCESS -> {
                    if (data != null) {
                        mViewModel.mFavoriteEstimateList = mViewModel.getEstimatesFromEtd(favorite, data.data.station.etdList)
                        mEtdAdapter = EstimateRecyclerAdapter(mViewModel.mFavoriteEstimateList)
                        binding.homeEtdRecyclerView.visibility = View.VISIBLE
                        binding.homeEtdRecyclerView.adapter = mEtdAdapter
                        binding.homeEtdRecyclerView.layoutManager = LinearLayoutManager(activity)
                    }
                }
                LiveDataWrapper.Status.ERROR -> {
                    //todo
                }
                else  -> {}
            }

        })
    }

    /*
        Fetches an ETD for the opposite direction of the favorite
     */
    private fun displayInverseEtd(inverse: Favorite) {
        mViewModel.getEtdLiveData(inverse.origin).observe(this, Observer { data ->
            if(data.status == LiveDataWrapper.Status.SUCCESS) {
                if (data != null) {
                    mViewModel.mInverseEstimateList = mViewModel.getEstimatesFromEtd(inverse, data.data.station.etdList)
                    mEtdInverseAdapter = EstimateRecyclerAdapter(mViewModel.mInverseEstimateList)
                    binding.homeEtdRecyclerView2.visibility = View.VISIBLE
                    binding.homeEtdRecyclerView2.adapter = mEtdInverseAdapter
                    binding.homeEtdRecyclerView2.layoutManager = LinearLayoutManager(activity)
                }
            }
            if(data.status == LiveDataWrapper.Status.ERROR) {
                //todo:
            }

            updateProgressBar()
        })
    }

    private fun loadWeather() {
        Logger.i("loading weather")

        mViewModel.getWeather().observe(this, Observer { response ->
            when(response.status) {
                LiveDataWrapper.Status.SUCCESS -> {
                    Logger.i("success")
                    displayWeather(response.data)
                }
                LiveDataWrapper.Status.ERROR -> {
                    Logger.e(response.msg)
                }
                else -> {
                    Logger.e( "WEATHER ERROR")
                }
            }
        })
    }

    //todo abstract to custom view
    private fun displayWeather(weather: WeatherResponse) {
        var textColor = resources.getColor(R.color.white) //default for daytime
        if(!mViewModel.isDaytime) textColor = resources.getColor(R.color.white)
        if(weather.name != null) { //city
            val nameTv = activity?.findViewById<TextView>(R.id.weather_name_tv)
            nameTv?.text = weather.name
            nameTv?.setTextColor(textColor)
        }

        if(weather.main != null) {
            if(weather.main?.temp != null) {
                val tempTv = activity?.findViewById<TextView>(R.id.weather_temp_tv)
                //° F = 9/5 (K - 273) + 32
                val imperialTemp = mViewModel.kelvinToFahrenheit(weather.main?.temp!!).roundToLong().toString()
                val temp = imperialTemp + resources.getString(R.string.weather_temp_imperial)
                tempTv?.text = temp
                tempTv?.setTextColor(textColor) //abstract
            }
        }

        if(weather.main?.humidity != null) {
            val humidtyTv = activity?.findViewById<TextView>(R.id.weather_humidity_tv)
            val humidity = "${weather.main?.humidity}%"
            humidtyTv?.text = humidity //todo use string resource
            humidtyTv?.setTextColor(textColor)
        }

        if(weather.wind != null) {
            val windTv = activity?.findViewById<TextView>(R.id.weather_wind_tv)
            val wind = "${weather.wind?.speed?.toString()}mph"
            windTv?.text = wind
            windTv?.setTextColor(textColor)
        }
    }

    private fun updateProgressBar() {
        binding.homeEtdProgressBar.visibility = View.GONE
    }

    //todo: add option for geolocation and to set "COMMUTE ROUTE" - morning/evening geolocation
    //https://openweathermap.org/current
}
