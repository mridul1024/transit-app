package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map

import android.Manifest
import android.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.databinding.FragmentGoogleMapBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.utils.AlertDialogUtils
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager
import com.zuk0.gaijinsmash.riderz.utils.ImageUtils
import com.zuk0.gaijinsmash.riderz.utils.NetworkUtils
import java.util.Objects

import javax.inject.Inject


//todo: implement search widget in toolbar?
class GoogleMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GoogleMapViewModel
    private lateinit var binding: FragmentGoogleMapBinding
    
    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    private var mImageView: ImageView? = null

    private val mContentRect: Rect? = null
    private val AXIS_X_MIN = 0
    private val AXIS_Y_MIN = 0
    private val AXIS_X_MAX = 10
    private val AXIS_Y_MAX = 10
    private val mCurrentViewport = RectF(AXIS_X_MIN.toFloat(), AXIS_Y_MIN.toFloat(), AXIS_X_MAX.toFloat(), AXIS_Y_MAX.toFloat())

    //---------------------------------------------------------------------------------------------
    // MapView must be used for Fragments to prevent nested fragments.
    //---------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        if (mapView != null)
            mapView?.onCreate(savedInstanceState)
        mScaleGestureDetector = ScaleGestureDetector(activity, ScaleListener())
        initGestureListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_google_map, container, false)
        mapView = binding.googleMapMapView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.collapseAppBar(activity)
        super.setTitle(activity, getString(R.string.menu_map))
        super.enableNestedScrolling(false)
        initMapView(savedInstanceState)
        initFab(view)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        //todo: close snackbars
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        super.enableNestedScrolling(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


    private fun initGestureListener() {
        val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                // Pixel offset is the offset in screen pixels, while viewport offset is the
                // offset within the current viewport.
                val viewportOffsetX = distanceX * mCurrentViewport.width() / mContentRect?.width() as Int
                val viewportOffsetY = -distanceY * mCurrentViewport.height() / mContentRect.height() as Int

                setViewportBottomLeft(mCurrentViewport.left + viewportOffsetX, mCurrentViewport.right + viewportOffsetY)
                return true
            }
        }
    }

    private fun setViewportBottomLeft(x: Float, y: Float) {
        var x = x
        var y = y
        val curWidth = mCurrentViewport.width()
        val curHeight = mCurrentViewport.height()
        x = Math.max(AXIS_X_MIN.toFloat(), Math.min(x, AXIS_X_MAX - curWidth))
        y = Math.max(AXIS_Y_MIN + curHeight, Math.min(y, AXIS_Y_MAX.toFloat()))

        mCurrentViewport.set(x, y - curHeight, x + curWidth, y)

        // Invalidates the View to update the display.
        ViewCompat.postInvalidateOnAnimation(Objects.requireNonNull<View>(this.view))
    }

    private fun initFab(view: View) {
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { v ->
            val builder = AlertDialog.Builder(activity)
            val view2 = layoutInflater.inflate(R.layout.bartmap_alert_dialog, null)
            mImageView = view2.findViewById(R.id.bartMap_custom_imageView)

            //todo: warning for accessibility users
            mImageView?.setOnTouchListener { view1, event ->
                mScaleGestureDetector?.onTouchEvent(event)
                true
            }

            viewModel.initBartMap(context, mImageView)
            builder.setView(view2)
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        try {
            mapView?.onCreate(savedInstanceState)
            mapView?.getMapAsync(this)
        } catch (e: Exception) {
            Log.i("Map View Error:", e.toString())
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GoogleMapViewModel::class.java)
    }

    //---------------------------------------------------------------------------------------------
    // Google Maps
    //---------------------------------------------------------------------------------------------

    override// This is called when google map is initialized
    fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        initMapSettings(googleMap)
        initUserLocation(activity, googleMap)
        initStationMarkers(googleMap) // Populate map with all the stations (markers)
        initMapOnClickListener(googleMap)

        // Move camera to specified Station from user's selection on StationInfoFragment
        val bundle = arguments
        viewModel.initLocationFromBundle(bundle, googleMap)
        mapView?.onResume()
    }

    private fun initMapOnClickListener(googleMap: GoogleMap) {
        googleMap.setOnMarkerClickListener { marker ->
            initMarkerSnackbar(googleMap, marker.position, marker.title)
            false
        }
    }

    private fun initMarkerSnackbar(map: GoogleMap, position: LatLng, destination: String) {
        val parentView = activity?.findViewById<View>(R.id.main_coordinatorLayout)
        val message = resources.getString(R.string.alert_dialog_gpsMarker)
        val yesAction = resources.getString(R.string.alert_dialog_yes)
        if(parentView != null) {
            Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.main_bottom_navigation)
                    .setAction(yesAction) { view ->
                        if (LocationManager.checkLocationPermission(activity)) {
                            val station = viewModel.findNearestMarker(map, position, destination, viewModel.stationList)
                            if (station == null) {
                                Toast.makeText(activity, "You are already near you destination", Toast.LENGTH_SHORT).show()
                            } else {
                                initTripAlertDialog(station, destination)
                            }
                        } else {
                            ActivityCompat.requestPermissions(
                                    activity as FragmentActivity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    LocationManager.LOCATION_PERMISSION_REQUEST_CODE)
                        }
                    }
                    .setActionTextColor(Color.RED)
                    .show()
        }
    }

    private fun initTripAlertDialog(station: Station, destination: String) {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_spinner, null)
        val destinationTv = view.findViewById<TextView>(R.id.googleMap_dialog_destinationTv)
        destinationTv.text = destination

        val stationsList = resources.getStringArray(R.array.stations_list)
        val adapter = ArrayAdapter(Objects.requireNonNull<FragmentActivity>(activity), android.R.layout.simple_spinner_dropdown_item, stationsList)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        val spinner = view.findViewById<Spinner>(R.id.dialog_origin_spinner)
        spinner.adapter = adapter
        spinner.setSelection(adapter.getPosition(station.name))

        AlertDialogUtils.launchGoogleMapDialog(
                view,
                this,
                spinner,
                station,
                destination)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(location: Location) {}

    private fun initMapSettings(map: GoogleMap) {
        viewModel.initMapSettings(map)
    }

    private fun initUserLocation(context: Context?, map: GoogleMap?) {
        val location: LocationManager
        var loc: Location? = null
        try {
            if (context != null && LocationManager.checkLocationPermission(context)) {
                location = LocationManager(context)
                loc = location.location
                map?.isMyLocationEnabled = true
                map?.setOnMyLocationButtonClickListener(this)
                map?.setOnMyLocationClickListener(this)
            } else {
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LocationManager.LOCATION_PERMISSION_REQUEST_CODE)
            }
        } catch (e: SecurityException) {
            Logger.e(e.message ?: "")
        }

        val gpsCheck = NetworkUtils.isGPSEnabled(context)
        if (gpsCheck && loc != null) {
            // move camera to user location
            val userLocation = LatLng(loc.latitude, loc.longitude)
            val update: CameraUpdate
            update = CameraUpdateFactory.newLatLngZoom(userLocation, 11f)
            map?.moveCamera(update)
        } else {
            if (map != null) {
                viewModel.initDefaultLocation(map)
            }
        }
    }

    private fun initStationMarkers(map: GoogleMap) {
        viewModel.stationsLiveData?.observe(this, Observer{ stationList ->
            viewModel.stationList = stationList
            if (stationList != null) {
                for (station in stationList) {
                    val latLng = LatLng(station.latitude, station.longitude)
                    map.addMarker(MarkerOptions()
                            .position(latLng)
                            .title(station.name)
                            .snippet(station.address)
                            .icon(ImageUtils.bitmapDescriptorFromVector(context!!, R.drawable.ic_train_24dp)))
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_train)));
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
            binding.googleMapProgressBar.visibility = View.GONE
        })
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 20.0f))
            mImageView?.scaleX = mScaleFactor
            mImageView?.scaleY = mScaleFactor
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.i("onRequestPermission", requestCode.toString())
        if (requestCode == LocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            Log.i("permission result", "101")
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUserLocation(activity, googleMap)
            } else {
                Log.wtf("permission result", "failed")
            }
        }
    }
}
