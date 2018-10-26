package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.databinding.ViewGoogleMapBinding;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map.BartMapFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.trip.TripFragment;
import com.zuk0.gaijinsmash.riderz.utils.AlertDialogUtils;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.NetworkUtils;
import com.zuk0.gaijinsmash.riderz.utils.debug.DebugController;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    @Inject
    GoogleMapViewModelFactory mViewModelFactory;

    private ViewGoogleMapBinding mDataBinding;
    private MapView mMapView;
    private GoogleMapViewModel mViewModel;
    private List<Station> mStationList;

    //---------------------------------------------------------------------------------------------
    // MapView must be used for Fragments to prevent nested fragments.
    //---------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (mMapView != null)
            mMapView.onCreate(bundle);
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

    //---------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    //---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_google_map, container, false);
        mMapView = mDataBinding.googleMapMapView;
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initMapView(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initBartButton();
    }

    private void initMapView(Bundle savedInstanceState) {
        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.i("Map View Error:", e.toString());
        }
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(GoogleMapViewModel.class);
    }

    private void initBartButton() {
        mDataBinding.googleMapBtn.setOnClickListener(v -> initBartMapFragment());
    }

    private void initBartMapFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tx;
        if (manager != null) {
            tx = manager.beginTransaction();
            Fragment newFrag = new BartMapFragment();
            tx.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
        }
    }

    //---------------------------------------------------------------------------------------------
    // Google Maps
    //---------------------------------------------------------------------------------------------

    @Override // This is called when google map is initialized
    public void onMapReady(GoogleMap googleMap) {
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
        View parentView = Objects.requireNonNull(getActivity()).findViewById(R.id.main_app_bar_coordinatorLayout);
        String message = getResources().getString(R.string.alert_dialog_gpsMarker);
        String yesAction = getResources().getString(R.string.alert_dialog_yes);
        Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(yesAction, view -> {
                    if(GpsUtils.checkLocationPermission(getActivity())) {
                        Station station = mViewModel.findNearestMarker(map, position, destination, mStationList);
                        if(station == null) {
                            Toast.makeText(getActivity(), "You are already near you destination", Toast.LENGTH_SHORT).show();
                        } else {
                            //todo: confirm user wants to use nearest bart station or, show dialog picker
                            initTripAlertDialog(station, destination);
                        }
                    } else {
                        AlertDialogUtils.launchLocationAlertDialog(getActivity(), parentView);
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    //todo: refactor this - abstract to AlertDialogUtils
    private void initTripAlertDialog(Station station, String destination) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_spinner, null);
        TextView destinationTv = view.findViewById(R.id.googleMap_dialog_destinationTv);
        destinationTv.setText(destination);

        String[] stationsList = getResources().getStringArray(R.array.stations_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, stationsList);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner spinner = view.findViewById(R.id.googleMap_dialog_spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(station.getName()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(getString(R.string.googleMap_dialog_title))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.alert_dialog_continue), (dialog, which) -> {
                    String origin = spinner.getSelectedItem().toString();
                    if(!origin.isEmpty()){
                        Bundle bundle = new Bundle();
                        bundle.putString(TripFragment.TripBundle.ORIGIN.getValue(), station.getName());
                        bundle.putString(TripFragment.TripBundle.DESTINATION.getValue(), destination);
                        bundle.putString(TripFragment.TripBundle.DATE.getValue(), "TODAY");
                        bundle.putString(TripFragment.TripBundle.TIME.getValue(), "NOW");
                        loadNewFragment(bundle);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel), (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void loadNewFragment(Bundle bundle) {
        Fragment newFrag = new BartResultsFragment();
        newFrag.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction;
        if (manager != null) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.fragmentContent, newFrag)
                    .addToBackStack(null).commit();
        }
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
        View parentView = Objects.requireNonNull(getActivity()).findViewById(R.id.main_app_bar_coordinatorLayout);
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
                AlertDialogUtils.launchLocationAlertDialog(context, parentView);
            }
        } catch(SecurityException e) {
            if(DebugController.LOG_E)
                Log.e("Exception: %s", e.getMessage());
        }

        boolean gpsCheck = NetworkUtils.isGPSEnabled(context);
        if (gpsCheck && loc != null) {
            // move camera to user location
            LatLng userLocation;
            userLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate update;
            update = CameraUpdateFactory.newLatLngZoom(userLocation, 6f);
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
}
