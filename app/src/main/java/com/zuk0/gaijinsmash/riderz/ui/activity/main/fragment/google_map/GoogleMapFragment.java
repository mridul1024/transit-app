package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map;

import android.Manifest;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.databinding.ViewGoogleMapBinding;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment;
import com.zuk0.gaijinsmash.riderz.utils.AlertDialogUtils;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


//todo: implement search widget in toolbar?
public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    @Inject
    GoogleMapViewModelFactory mViewModelFactory;

    private ViewGoogleMapBinding mDataBinding;
    private MapView mMapView;
    private GoogleMapViewModel mViewModel;
    private List<Station> mStationList; //todo put in viewmodel

    private GoogleMap mGoogleMap;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private ImageView mImageView;
    private Rect mContentRect;
    private int AXIS_X_MIN = 0;
    private int AXIS_Y_MIN = 0;
    private int AXIS_X_MAX = 10;
    private int AXIS_Y_MAX = 10;
    private RectF mCurrentViewport =
            new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

    private static final int LOCATION_PERMISSON_REQUEST_CODE = 101;

    //---------------------------------------------------------------------------------------------
    // MapView must be used for Fragments to prevent nested fragments.
    //---------------------------------------------------------------------------------------------

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (mMapView != null)
            mMapView.onCreate(bundle);
        mScaleGestureDetector = new ScaleGestureDetector(getActivity(), new GoogleMapFragment.ScaleListener());
        initGestureListener();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_google_map, container, false);
        mMapView = mDataBinding.googleMapMapView;
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initMapView(savedInstanceState);
        initFab(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        super.collapseAppBar(getActivity());
        getLifecycle().addObserver(new GoogleMapObserver());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
        //todo: close snackbars
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null)
            mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }


    private void initGestureListener() {
        GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Pixel offset is the offset in screen pixels, while viewport offset is the
                // offset within the current viewport.
                float viewportOffsetX = distanceX * mCurrentViewport.width()
                        / mContentRect.width();
                float viewportOffsetY = -distanceY * mCurrentViewport.height()
                        / mContentRect.height();

                setViewportBottomLeft(mCurrentViewport.left + viewportOffsetX, mCurrentViewport.right + viewportOffsetY);
                return true;
            }
        };
    }

    private void setViewportBottomLeft(float x, float y) {
        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        x = Math.max(AXIS_X_MIN, Math.min(x, AXIS_X_MAX - curWidth));
        y = Math.max(AXIS_Y_MIN + curHeight, Math.min(y, AXIS_Y_MAX));

        mCurrentViewport.set(x, y - curHeight, x + curWidth, y);

        // Invalidates the View to update the display.
        ViewCompat.postInvalidateOnAnimation(Objects.requireNonNull(this.getView()));
    }

    private void initFab(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view2 = getLayoutInflater().inflate(R.layout.bartmap_alert_dialog, null);
            mImageView = view2.findViewById(R.id.bartMap_custom_imageView);

            //todo: warning for accessibility users
            mImageView.setOnTouchListener((view1, event) -> {
                mScaleGestureDetector.onTouchEvent(event);
                return true;
            });

            mViewModel.initBartMap(getActivity(),mImageView);
            builder.setView(view2);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void initMapView(Bundle savedInstanceState) {
        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.i("Map View Error:", e.toString());
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(GoogleMapViewModel.class);
    }

    //---------------------------------------------------------------------------------------------
    // Google Maps
    //---------------------------------------------------------------------------------------------

    @Override // This is called when google map is initialized
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        initMapSettings(googleMap);
        initUserLocation(getActivity(), googleMap);
        initStationMarkers(googleMap); // Populate map with all the stations (markers)
        initMapOnClickListener(googleMap);

        // Move camera to specified Station from user's selection on StationInfoFragment
        Bundle bundle = getArguments();
        mViewModel.initLocationFromBundle(bundle, googleMap);
        mMapView.onResume();
    }

    private void initMapOnClickListener(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(marker -> {
            initMarkerSnackbar(googleMap, marker.getPosition(), marker.getTitle());
            return false;
        });
    }

    private void initMarkerSnackbar(GoogleMap map, LatLng position, String destination) {
        View parentView = Objects.requireNonNull(getActivity()).findViewById(R.id.main_coordinatorLayout);
        String message = getResources().getString(R.string.alert_dialog_gpsMarker);
        String yesAction = getResources().getString(R.string.alert_dialog_yes);
        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .setAction(yesAction, view -> {
                    if(GpsUtils.checkLocationPermission(getActivity())) {
                        Station station = mViewModel.findNearestMarker(map, position, destination, mStationList);
                        if(station == null) {
                            Toast.makeText(getActivity(), "You are already near you destination", Toast.LENGTH_SHORT).show();
                        } else {
                            initTripAlertDialog(station, destination);
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSON_REQUEST_CODE);
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    private void initTripAlertDialog(Station station, String destination) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_spinner, null);
        TextView destinationTv = view.findViewById(R.id.googleMap_dialog_destinationTv);
        destinationTv.setText(destination);

        String[] stationsList = getResources().getStringArray(R.array.stations_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, stationsList);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner spinner = view.findViewById(R.id.dialog_origin_spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(station.getName()));

        AlertDialogUtils.launchGoogleMapDialog(
                view,
                this,
                spinner,
                station,
                destination);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    private void initMapSettings(GoogleMap map) {
        mViewModel.initMapSettings(map);
    }

    private void initUserLocation(Context context, GoogleMap map) {
        GpsUtils gps;
        Location loc = null;
        try {
            if(GpsUtils.checkLocationPermission(context)) {
                gps = new GpsUtils(context);
                loc = gps.getLocation();
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.setOnMyLocationClickListener(this);
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSON_REQUEST_CODE);
            }
        } catch(SecurityException e) {
            Logger.e(e.getMessage());
        }

        boolean gpsCheck = NetworkUtils.isGPSEnabled(context);
        if (gpsCheck && loc != null) {
            // move camera to user location
            LatLng userLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate update;
            update = CameraUpdateFactory.newLatLngZoom(userLocation, 11f);
            map.moveCamera(update);
        } else {
            mViewModel.initDefaultLocation(map);
        }
    }

    private void initStationMarkers(GoogleMap map) {
        mViewModel.getStationsLiveData().observe(this, stationList -> {
            mStationList = stationList;
            if (stationList != null) {
                for(Station station : stationList) {
                    LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(station.getName())
                            .snippet(station.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
            mDataBinding.googleMapProgressBar.setVisibility(View.GONE);
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 20.0f));
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("onRequestPermission", String.valueOf(requestCode));
        switch(requestCode) {
            case LOCATION_PERMISSON_REQUEST_CODE: {
                Log.i("permission result", "101");
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initUserLocation(getActivity(),mGoogleMap);
                } else{
                    Log.wtf("permission result", "failed");
                }
                break;
            }
        }
    }
}
