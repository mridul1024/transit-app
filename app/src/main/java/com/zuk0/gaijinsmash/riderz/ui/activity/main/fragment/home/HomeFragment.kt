package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.BsaRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EstimateRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.AlertDialogUtils
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_home.view.*
import javax.inject.Inject
import kotlin.math.roundToLong

//todo add option to get estimates based on user location - create a commute route. preferred origin and destination.
//get user location - if user location is at a bart station other than the preferred, get etd from that location instead.
// allow user to create a HOME(s) station.
//show info based on user location to either location. or button to switch

class HomeFragment : Fragment() {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory

    private lateinit var mDataBinding: com.zuk0.gaijinsmash.riderz.databinding.ViewHomeBinding
    private lateinit var mViewModel: HomeViewModel

    private var mInverseEstimateList: List<Estimate>? = null //todo refactor - put in viewmodel
    private var mFavoriteEstimateList: List<Estimate>? = null //todo refactor - put in viewmodel
    private var mEtdAdapter: EstimateRecyclerAdapter? = null //todo refactor - put in viewmodel
    private var mEtdInverseAdapter: EstimateRecyclerAdapter? = null //todo refactor - put in viewmodel

    private lateinit var mCallToAction: CallToActionView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.initDagger()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_home, container, false)
        return mDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.initViewModel()
        loadAdvisories(mViewModel.bsaLiveData)
        initFavoriteObserver()
        loadWeather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainAppBarLayout = activity?.findViewById<AppBarLayout>(R.id.main_app_bar_layout)
        mainAppBarLayout?.setExpanded(true)
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

    private fun initDagger() {
        AndroidSupportInjection.inject(this)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel::class.java)
    }

    private fun expandBottomNavView() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        HideBottomViewOnScrollBehavior<BottomNavigationView>(activity, null).slideUp(bottomNav)
    }

    /*
       A way to provide news that cannot be fetched by the api via webview.
       CardView's visibility is GONE by default
     */
    private fun displayNews(isActive: Boolean) {
        if(isActive) {
            mDataBinding.homeNewsCardView.visibility = View.VISIBLE
            val newsButton = activity?.findViewById<Button>(R.id.home_news_button)
            newsButton?.setOnClickListener { //todo refactor, create a custom view to inject data easily
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_newsFragment)
            }

            val title = resources.getString(R.string.developer_update)
            val msg = resources.getString(R.string.app_crash)

            if(SharedPreferencesUtils.getDevUpdatePreference(activity)) AlertDialogUtils.launchNotificationDialog(activity, title, msg)
        }
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
    */
    private fun loadAdvisories(bsa: LiveData<BsaXmlResponse>) {
        bsa.observe(this, Observer { bsaXmlResponse ->
            if (bsaXmlResponse != null) {
                val bsaAdapter = BsaRecyclerAdapter(bsaXmlResponse.bsaList)
                mDataBinding.homeBsaRecyclerView.adapter = bsaAdapter
                mDataBinding.homeBsaRecyclerView.layoutManager = LinearLayoutManager(activity)
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

    private fun loadCallToAction(isFavoriteAvailable: Boolean) { //todo: verify
        if(!isFavoriteAvailable) {
            if(!mDataBinding.homeStubCallToAction.isInflated)
                mDataBinding.homeStubCallToAction.viewStub?.inflate()
        }
    }

    private fun displayFavoriteEtd(favorite: Favorite) {
        mViewModel.getEtdLiveData(favorite.origin).observe(this, Observer { data ->
            if (data != null) {
                mFavoriteEstimateList = mViewModel.getEstimatesFromEtd(favorite, data.station.etdList)
                mEtdAdapter = EstimateRecyclerAdapter(mFavoriteEstimateList)
                mDataBinding.homeEtdRecyclerView.visibility = View.VISIBLE
                mDataBinding.homeEtdRecyclerView.adapter = mEtdAdapter
                mDataBinding.homeEtdRecyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    /*
        Fetches an ETD for the opposite direction of the favorite
     */
    private fun displayInverseEtd(inverse: Favorite) {
        mViewModel.getEtdLiveData(inverse.origin).observe(this, Observer { data ->
            if (data != null) {
                mInverseEstimateList = mViewModel.getEstimatesFromEtd(inverse, data.station.etdList)
                mEtdInverseAdapter = EstimateRecyclerAdapter(mInverseEstimateList)
                mDataBinding.homeEtdRecyclerView2.visibility = View.VISIBLE
                mDataBinding.homeEtdRecyclerView2.adapter = mEtdInverseAdapter
                mDataBinding.homeEtdRecyclerView2.layoutManager = LinearLayoutManager(activity)
            }
            updateProgressBar()
        })
    }

    private fun loadWeather() {
        Logger.i("loading weather")
        //todo get user location if available, else use a default location

        mViewModel.getWeather(94526).observe(this, Observer { response ->
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

    private fun displayWeather(weather: WeatherResponse) {
        var textColor = resources.getColor(R.color.bartYellowLine) //default for daytime
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
        mDataBinding.homeEtdProgressBar.visibility = View.GONE
    }

    //todo: add option for geolocation and to set "COMMUTE ROUTE" - morning/evening geolocation
    //https://openweathermap.org/current
}
