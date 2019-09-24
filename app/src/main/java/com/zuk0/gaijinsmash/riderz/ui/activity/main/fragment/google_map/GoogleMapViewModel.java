package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.google_map;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.utils.GpsUtils;
import com.zuk0.gaijinsmash.riderz.utils.HaversineFormulaUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GoogleMapViewModel extends AndroidViewModel {

    // Map Constraints
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
        map.setMinZoomPreference(10f);
    }

    void initBartMap(Context context, ImageView imageView) {

        Drawable img;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 7) {
            img = context.getDrawable(R.drawable.bart_map_sunday);
        } else {
            img = context.getDrawable(R.drawable.bart_map_weekday_sat);
        }
        Glide.with(context)
                .load(img)
                .into(imageView);
    }

    //todo: refactor as this is inside  another class now.
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

                int distanceBetween = HaversineFormulaUtils.INSTANCE.calculateDistanceInKilometer(userLocation.getLatitude(), userLocation.getLongitude(),
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
                Log.i("closestStation", "USER IS ALREADY THERE");
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
            LatLng latLng = new LatLng(Double.valueOf(Objects.requireNonNull(bundle.getString("StationLat"))), Double.valueOf(Objects.requireNonNull(bundle.getString("StationLong"))));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
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
