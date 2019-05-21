package com.zuk0.gaijinsmash.riderz.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;

/*
    TODO: add lifecycle handling
 */
public class GpsUtils implements LocationListener {

    private String TAG = "GpsUtils";
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private String mLocationProvider;


    /*
        Handle user location permissions here?
     */
    public GpsUtils(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Define the criteria for how to select the location provider


        mLocationProvider = initLocationProvider(context, manager, criteria);
        mLocation = initLocation(manager, context);

        // Initialize the location fields
        if(mLocation != null) {
            onLocationChanged(mLocation);
        } else {
            Log.i("GPS:", "Location is not available");
        }
    }

    public Location getLocation() {
        return mLocation;
    }
    public String getLocationProvider() { return mLocationProvider; }

    public static boolean checkLocationPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private String initLocationProvider(Context context, LocationManager manager, Criteria criteria) {
        String locationProvider = "";
        if(checkLocationPermission(context)) {
            if (manager != null) {
                locationProvider = manager.getBestProvider(criteria, true);
                Log.i(TAG, locationProvider);
            }
        }
        return locationProvider;
    }

    private Location initLocation(LocationManager manager, Context context) {
        Location location = null;
        if(checkLocationPermission(context)) {
            location = manager.getLastKnownLocation(mLocationProvider);
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = mLocation.getLatitude();
        mLongitude = mLocation.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
} // End of Class
