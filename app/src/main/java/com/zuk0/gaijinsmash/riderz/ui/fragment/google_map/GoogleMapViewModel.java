package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.NetworkUtils;
import com.zuk0.gaijinsmash.riderz.utils.debug.DebugController;

import java.util.List;

class GoogleMapViewModel extends AndroidViewModel {

    private static LatLng SOUTH_WEST_BOUNDS = new LatLng(37.2982, -121.5363);
    private static LatLng NORTH_EAST_BOUNDS = new LatLng(38.0694, -121.7438);
    private static LatLng DEFAULT_LOCATION = new LatLng(37.73659478, -122.19683306);
    private static float DEFAULT_ZOOM = 6f;

    private LiveData<List<Station>> mStationsLivedata;

    GoogleMapViewModel(Application application) {
        super(application);
    }

    // For Google Map
    void initMapSettings(GoogleMap map) {
        Log.i("initMapSettings", "googlemap");
        // Set boundary of map area
        LatLngBounds bayArea = new LatLngBounds(
                SOUTH_WEST_BOUNDS,
                NORTH_EAST_BOUNDS);
        map.setLatLngBoundsForCameraTarget(bayArea);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.setMinZoomPreference(9f);
    }

    void initMarkerSnackbar(View parentView, List<Station> list) {
        String message = getApplication().getResources().getString(R.string.alert_dialog_gpsMarker);
        String yesAction = getApplication().getResources().getString(R.string.alert_dialog_yes);
        Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(yesAction, view -> {
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    Station findNearestMarker(GoogleMap map, LatLng destination, List<Station> list) {
        // get user location
        GpsUtils gps = new GpsUtils(getApplication());
        Location userLocation = gps.getLocation();

        // keep track of closest station
        LatLng closestStationLatlng = null; // initial value
        Station closestStation = null;
        if(userLocation != null) {
            // else, calculate distance to closest station
            for(Station station : list) {
                double stationLatitude = station.getLatitude();
                double stationLongitude = station.getLongitude();

                double latitudeDifference = userLocation.getLatitude() - stationLatitude;
                double longitudeDifference = userLocation.getLongitude() - stationLongitude;
                if(closestStationLatlng == null) {
                    closestStationLatlng = new LatLng(stationLatitude, stationLongitude);
                    closestStation = station;
                } else {
                    // compare results - if the current station is closer than the saved variable, replace
                    if(closestStationLatlng.latitude - userLocation.getLatitude() > latitudeDifference &&
                            closestStationLatlng.longitude - userLocation.getLongitude() > longitudeDifference) {
                        closestStationLatlng = new LatLng(stationLatitude, stationLongitude);
                        closestStation = station;
                        Log.i("closest station:", station.getName());
                    }
                }
            }
            // if user is already near destination station, alert user
            if(destination == closestStationLatlng) {
                return null;
            }
        } else {
            return null;
        }

        return closestStation;
    }


    void initLocationFromBundle(Bundle bundle, GoogleMap googleMap) {
        if(bundle != null) {
            String stationTitle = bundle.getString("StationTitle");
            LatLng latLng = new LatLng(Double.valueOf(bundle.getString("StationLat")), Double.valueOf(bundle.getString("StationLong")));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(stationTitle));
            marker.showInfoWindow();
        }
    }

    void initUserLocation(Context context, View parentView, GoogleMap map) {
        GpsUtils gps;
        Location loc = null;
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gps = new GpsUtils(context);
                loc = gps.getLocation();
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(getApplication()); //todo: test this
                map.setOnMyLocationClickListener(getApplication()); //todo: test this
            } else {
                String message = getApplication().getResources().getString(R.string.gps_permission_alert);
                String yesAction = getApplication().getResources().getString(R.string.alert_dialog_yes);
                Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(yesAction, view -> {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        })
                        .setActionTextColor(Color.RED)
                        .show();
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
            update = CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM);
            map.moveCamera(update);
        } else {
            initDefaultLocation(map);
        }
    }

    public void initDefaultLocation(GoogleMap map) {
        LatLng defaultLocation = DEFAULT_LOCATION;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
    }

    public LiveData<List<Station>> getStationsLiveData() {
        if(mStationsLivedata == null) {
            mStationsLivedata = StationDatabase.getRoomDB(getApplication()).getStationDAO().getStationsLiveData();
        }
        return mStationsLivedata;
    }
}
