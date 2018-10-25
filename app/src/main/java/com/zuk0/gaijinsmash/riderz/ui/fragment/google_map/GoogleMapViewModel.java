package com.zuk0.gaijinsmash.riderz.ui.fragment.google_map;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.HaversineFormulaUtils;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GoogleMapViewModel extends AndroidViewModel {

    private static LatLng SOUTH_WEST_BOUNDS = new LatLng(37.2982, -121.5363);
    private static LatLng NORTH_EAST_BOUNDS = new LatLng(38.0694, -121.7438);
    private static LatLng DEFAULT_LOCATION = new LatLng(37.73659478, -122.19683306);

    private LiveData<List<Station>> mStationsLivedata;

    @Inject
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

    Station findNearestMarker(GoogleMap map, LatLng destinationCoordinates, String destinationStation, List<Station> list) {
        // get user location
        GpsUtils gps = new GpsUtils(getApplication());
        Location userLocation = gps.getLocation();

        // keep track of closest station
        Station closestStation = null;
        int closestDistance = 0;

        if(userLocation != null) {
            // else, calculate distance to closest station
            for(Station station : list) {
                double stationLatitude = station.getLatitude();
                double stationLongitude = station.getLongitude();

                int distanceBetween = HaversineFormulaUtils.calculateDistanceInKilometer(userLocation.getLatitude(), userLocation.getLongitude(),
                                                                    stationLatitude, stationLongitude);
                if(closestDistance == 0) {
                    closestDistance = distanceBetween;
                    closestStation = station;
                } else {
                    if(closestDistance > distanceBetween) {
                        closestDistance = distanceBetween;
                        closestStation = station;
                        Log.i("closestStation", station.getName());
                    }
                }
            }
            // if user is already near destination station, alert user
            if(destinationStation.equalsIgnoreCase(Objects.requireNonNull(closestStation).getName())) {
                Log.i("closesstStation", "USER IS ALREADY THERE");
                return null;
            }
        } else {
            Log.i("userLoc", "NULL");
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

    void initDefaultLocation(GoogleMap map) {
        LatLng defaultLocation = DEFAULT_LOCATION;
        float DEFAULT_ZOOM = 6f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
    }

    LiveData<List<Station>> getStationsLiveData() {
        if(mStationsLivedata == null) {
            mStationsLivedata = StationDatabase.getRoomDB(getApplication()).getStationDAO().getStationsLiveData();
        }
        return mStationsLivedata;
    }
}
