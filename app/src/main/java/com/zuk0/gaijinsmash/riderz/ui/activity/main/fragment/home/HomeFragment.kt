package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper
import com.zuk0.gaijinsmash.riderz.utils.AlertDialogUtils
import com.zuk0.gaijinsmash.riderz.utils.SharedPreferencesUtils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory

    private lateinit var mDataBinding: com.zuk0.gaijinsmash.riderz.databinding.ViewHomeBinding
    private lateinit var mViewModel: HomeViewModel

    private var mInverseEstimateList: List<Estimate>? = null
    private var mFavoriteEstimateList: List<Estimate>? = null
    private var mEtdAdapter: EstimateRecyclerAdapter? = null
    private var mEtdInverseAdapter: EstimateRecyclerAdapter? = null

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
        this.initDagger()
        this.initViewModel()
        loadAdvisories(mViewModel.bsaLiveData)
        initFavoriteObserver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainAppBarLayout = activity?.findViewById<AppBarLayout>(R.id.main_app_bar_layout)
        mainAppBarLayout?.setExpanded(true)
        expandBottomNavView()
        initNews()
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
       A way to provide news that cannot be fetched by the api via webview
     */
    private fun initNews() {
        val newsButton = activity?.findViewById<Button>(R.id.home_news_button)
        newsButton?.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_newsFragment)
        }

        val title = resources.getString(R.string.developer_update)
        val msg = resources.getString(R.string.app_crash)

        if(SharedPreferencesUtils.getDevUpdatePreference(activity)) AlertDialogUtils.launchNotificationDialog(activity, title, msg)
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
    */
    private fun loadAdvisories(bsa: LiveData<BsaXmlResponse>) {
        bsa.observe(this, Observer { bsaXmlResponse ->
            if (bsaXmlResponse != null) {
                mDataBinding.bsaViewTimeTv.text = mViewModel.initMessage(Objects.requireNonNull<FragmentActivity>(activity), mViewModel.is24HrTimeOn(activity), bsaXmlResponse.time)
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
                        loadTripData(fav)
                        loadInverseTripData(fav)
                    }

                    override fun onComplete() {
                        Log.i("onComplete","completed")
                        updateProgressBar()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("onError", e.message)
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
                    loadFavoriteEtd(favorite)
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
                    loadInverseEtd(inverse)
                }
            }

            if(status == LiveDataWrapper.Status.ERROR) {
                updateProgressBar()
            }
        })
    }

    private fun loadFavoriteEtd(favorite: Favorite) {
        mViewModel.getEtdLiveData(favorite.origin).observe(this, Observer { data ->
            if (data != null) {
                mFavoriteEstimateList = mViewModel.getEstimatesFromEtd(favorite, data.station.etdList)
                mEtdAdapter = EstimateRecyclerAdapter(mFavoriteEstimateList)
                mDataBinding.homeEtdRecyclerView.adapter = mEtdAdapter
                mDataBinding.homeEtdRecyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    /*
        Fetches an ETD for the opposite direction of the favorite
     */
    private fun loadInverseEtd(inverse: Favorite) {
        mViewModel.getEtdLiveData(inverse.origin).observe(this, Observer { data ->
            if (data != null) {
                mInverseEstimateList = mViewModel.getEstimatesFromEtd(inverse, data.station.etdList)
                mEtdInverseAdapter = EstimateRecyclerAdapter(mInverseEstimateList)
                mDataBinding.homeEtdRecyclerView2.adapter = mEtdInverseAdapter
                mDataBinding.homeEtdRecyclerView2.layoutManager = LinearLayoutManager(activity)
            }
            updateProgressBar()
        })
    }

    private fun updateProgressBar() {
        mDataBinding.homeEtdProgressBar.visibility = View.GONE
    }
}
