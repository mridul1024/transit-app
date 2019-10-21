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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import com.zuk0.gaijinsmash.riderz.databinding.FragmentHomeBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.BsaRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.EstimateRecyclerAdapter
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.presenter.HomeWeatherPresenter
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils
import com.zuk0.gaijinsmash.riderz.utils.PermissionUtils
import com.zuk0.gaijinsmash.riderz.utils.PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.expandAppBar(activity)
        initAdvisories(mViewModel.bsaLiveData)
        initUserLocation(context)
        expandBottomNavView()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
             LOCATION_PERMISSION_REQUEST_CODE -> {
                 if(permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     binding.homePermissionContainer.visibility = View.GONE
                     mViewModel.isLocationPermissionEnabledLD.postValue(true)
                 }
                 else
                     loadPermissionView()
             }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /*
        Helper Methods
     */

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel::class.java)
    }

    private fun expandBottomNavView() { // todo why?
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        bottomNav?.let {
            HideBottomViewOnScrollBehavior<BottomNavigationView>(activity, null).slideUp(bottomNav)
        }
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
    */
    private fun initAdvisories(bsa: LiveData<BsaXmlResponse>) {
        bsa.observe(this, Observer { bsaXmlResponse ->
            if (bsaXmlResponse != null) {
                binding.homeBsaRecyclerView.visibility = View.VISIBLE
                val bsaAdapter = BsaRecyclerAdapter(bsaXmlResponse.bsaList)
                binding.homeBsaRecyclerView.adapter = bsaAdapter
                binding.homeBsaRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        })
    }

    private fun initUserLocation(context: Context?) {
        if (ContextCompat.checkSelfPermission(context as Context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.isLocationPermissionEnabledLD.postValue(true)
        } else {
            if(GpsUtils.checkIfExplanationIsNeeded(activity)) {
                //show explanation if user has denied request before
                showExplanationToUser()
            } else {
                //request permission
                activity?.let {
                    ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE)
                }
            }
        }
    }

    /**
     * Display permission cardView when Location Permissions is off.
     */
    private fun loadPermissionView() {
        binding.homePermissionContainer.visibility = View.VISIBLE

        binding.enablePermissionButton.setOnClickListener { v ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, android.net.Uri.parse("package:" + activity?.packageName))
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE)
        }
        binding.disablePermissionButton.setOnClickListener {
            binding.homePermissionContainer.visibility = View.GONE
        }
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
            this.startActivityForResult(intent, GpsUtils.LOCATION_REQUEST_CODE)
            dialog.dismiss()
        }
        alert.setCancelable(true)
        alert.setNegativeButton(DialogInterface.BUTTON_NEGATIVE) { dialog, which ->
            dialog.dismiss()
        }
        alert.show()
    }
}
