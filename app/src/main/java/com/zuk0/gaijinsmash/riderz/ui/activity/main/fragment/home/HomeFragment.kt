package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.databinding.FragmentHomeBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.bsa.BsaRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter.HomeEtdPresenter
import com.zuk0.gaijinsmash.riderz.ui.shared.permission.PermissionPresenter
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragment
import javax.inject.Inject

//TODO add Trip Schedule for Favorites instead of ETDS
//show ETDS for only the local station.
//todo add option to get estimates based on user location - create a commute route. preferred origin and destination.
//get user location - if user location is at a bart station other than the preferred, get etd from that location instead.
//allow user to create a HOME(s) station.
//show info based on user location to either location. or button to switch

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var homeViewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var permissionPresenter: PermissionPresenter
    private lateinit var estimatePresenter: HomeEtdPresenter
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initViewModel()
        savedInstanceState?.let{ viewModel.restoreState(savedInstanceState) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.expandAppBar(activity)
        super.setTitle(activity, getString(R.string.app_name))
        initAdvisories(viewModel.bsaLiveData)
        initUserLocation(context)
        initLocationHandler(viewModel.isLocationPermissionEnabledLD)
        initNavigationObserver()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
             LOCATION_PERMISSION_REQUEST_CODE -> {
                 if(permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     viewModel.isLocationPermissionEnabledLD.postValue(true)
                 }
             }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /*
        Helper Methods
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
    */
    private fun initAdvisories(bsa: LiveData<BsaXmlResponse>) {
        bsa.observe(viewLifecycleOwner, Observer { bsaXmlResponse ->
            if (bsaXmlResponse != null) {
                binding.homeBsaRecyclerView.visibility = View.VISIBLE
                val bsaAdapter = BsaRecyclerAdapter(bsaXmlResponse.bsaList
                        ?: mutableListOf())
                binding.homeBsaRecyclerView.adapter = bsaAdapter
                binding.homeBsaRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        })
        /*viewModel.getBsaJson().observe(viewLifecycleOwner, Observer<BsaJsonResponse> {data ->
            binding.homeBsaRecyclerView.visibility = View.VISIBLE
            val size = data.root?.bsaList?.size
            val bsaAdapter = BsaRecyclerAdapter(data.root?.bsaList ?: mutableListOf())
            binding.homeBsaRecyclerView.adapter = bsaAdapter
            binding.homeBsaRecyclerView.layoutManager = LinearLayoutManager(context)

            Logger.i("size: $size")
        })*/
    }

    private fun initUserLocation(context: Context?) {
        if (ContextCompat.checkSelfPermission(context as Context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.isLocationPermissionEnabledLD.postValue(true)
        } else {
            if(LocationManager.checkIfExplanationIsNeeded(activity)) {
                showExplanationToUser()
            } else {
                //request permission
                activity?.let {
                    ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_CODE)
                }
            }
        }
    }

    private fun initLocationHandler(permissionLiveData: LiveData<Boolean>) {
        permissionLiveData.observe(viewLifecycleOwner, Observer { result ->
            if(result) {
                context?.let{
                    locationManager = LocationManager(it)
                    initEstimatesPresenter(activity)
                }
            } else {
                Logger.d("Permission Denied for Location")
            }
        })
    }

    private fun initEstimatesPresenter(context: Context?) {
        estimatePresenter = HomeEtdPresenter(context, binding.estimatePresenterLayout, viewModel)
        lifecycle.addObserver(estimatePresenter)
    }

    /**
     * Display permission cardView when Location Permissions is off.
     * Check
     */
    private fun loadPermissionView() {
        permissionPresenter = PermissionPresenter(activity, viewModel)
        permissionPresenter.showDialog()
    }

    /**
     * Permission Dialog
     */
    private fun showExplanationToUser() {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(context?.getString(R.string.title_permission_location))
        alert.setMessage(context?.getString(R.string.explanation_location))
        alert.setPositiveButton(DialogInterface.BUTTON_POSITIVE) { dialog, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context?.packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            this.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE)
            dialog.dismiss()
        }
        alert.setCancelable(true)
        alert.setNegativeButton(DialogInterface.BUTTON_NEGATIVE) { dialog, which ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun initNavigationObserver() {
        viewModel.navigationLiveData.observe(viewLifecycleOwner, Observer { tag ->
            when(tag) {
                StationInfoFragment.TAG -> {
                    viewModel.navigationLiveData.postValue("")
                    val args = Bundle()
                    args.putString(StationInfoFragment.STATION_INFO_EXTRA, viewModel.closestStation?.abbr)
                    NavHostFragment.findNavController(this).navigate(
                            R.id.action_homeFragment_to_stationInfoFragment,
                            args, null, null
                    )

                }
                else -> Logger.e("unhandled tag: $tag")
            }
        })
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
