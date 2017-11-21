package com.example.gaijinsmash.transitapp.network;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class FetchGPS extends Activity implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;     // flag for GPS status
    boolean isNetworkEnabled = false;    // flag for network status
    protected LocationManager locationManager;
    private Location mLocation;
    double mLatitude;
    double mLongitude;
    private String provider;
    private static final long LOCATION_REFRESH_DIST = 10; // 10 meters
    private static final long LOCATION_REFRESH_TIME = 1000 * 60 * 1; // 1 minute

    public FetchGPS(Context context) {
        this.mContext = context;
        //getLocation();
    }

    public Context getContext() {
        return mContext;
    }
    public Double getLatitude() {
        return mLatitude;
    }
    public Double getLongitude() {
        return mLongitude;
    }

    public static boolean checkGPSPermission(Context context) {
        boolean result = false;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            result = true;
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        return result;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        // Get the location manager
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        // get the GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // get network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled) {
            //TODO: create an AlarmDialog to prompt user
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        if (!isNetworkEnabled) {
            // Alert user to turn check network settings
            Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
            startActivity(intent);
        }
        if(isGPSEnabled && isNetworkEnabled) {
            // Define the criteria for how to select the location provider
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            mLocation = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if(mLocation != null) {
                Log.i("GPS:", "Provider " + provider + " has been selected");
                onLocationChanged(mLocation);
            } else {
                Log.i("GPS:", "Location is not available");
            }
        }
        return mLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = (int) (mLocation.getLatitude());
        mLongitude = (int) (mLocation.getLongitude());
        Log.i("Latitude", (String.valueOf(mLatitude)));
        Log.i("Longitude", (String.valueOf(mLongitude)));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("New Provider:", "ENABLED");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("Provider:", "DISABLED");
    }
} // End of Class
