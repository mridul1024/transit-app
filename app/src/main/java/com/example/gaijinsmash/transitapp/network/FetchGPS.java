package com.example.gaijinsmash.transitapp.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;


public class FetchGPS extends Activity {

    private static final boolean DEBUG = true;
    private final Context mContext;
    boolean isGPSEnabled = false;     // flag for GPS status
    boolean isNetworkEnabled = false;    // flag for network status
    Location location;
    double mLatitude;
    double mLongitude;
    private static final long LOCATION_REFRESH_DIST = 10; // 10 meters
    private static final long LOCATION_REFRESH_TIME = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    public FetchGPS(Context context) {
        this.mContext = context;
        //getLocation();
    }

    public Context getContext() {
        return mContext;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
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
            FetchLocationListener locationListener = new FetchLocationListener();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DIST, locationListener);
        }
        return location;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }
} // End of Class
