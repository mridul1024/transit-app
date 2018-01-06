package com.example.gaijinsmash.transitapp.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


public class FetchGPS extends Activity implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;     // flag for GPS status
    boolean isNetworkEnabled = false;    // flag for network status
    protected LocationManager mLocationManager;
    private Location mLocation;
    double mLatitude;
    double mLongitude;

    public FetchGPS(Context context) {
        this.mContext = context;
        initLocation();
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
    public Location getLocation() { return mLocation; }

    public static boolean checkGPSPermission(Context context) {
        boolean result = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            result = false;
        }
        return result;
    }

    public void initLocation() {
        // Get the location manager
        mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        // get the GPS status and Network status
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("isGPSEnabled : ", String.valueOf(isGPSEnabled));
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("isNetworkEnabled :", String.valueOf(isNetworkEnabled));
        if(!isGPSEnabled) {
            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivity(intent);
        }
        if (!isNetworkEnabled) {
            //TODO: alertdialog
            Toast.makeText(getContext(), "Network Disabled", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
            //startActivity(intent);
        }
        if(isGPSEnabled && isNetworkEnabled) {
            // Define the criteria for how to select the location provider
            Criteria criteria = new Criteria();
            String provider = mLocationManager.getBestProvider(criteria, true);
            if(checkGPSPermission(getContext())){
                mLocation = mLocationManager.getLastKnownLocation(provider);
            }
            // Initialize the location fields
            if(mLocation != null) {
                Log.i("GPS:", "Provider " + provider + " has been selected");
                onLocationChanged(mLocation);
            } else {
                Log.i("GPS:", "Location is not available");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = mLocation.getLatitude();
        Log.i("Latitude", (String.valueOf(mLatitude)));
        mLongitude = mLocation.getLongitude();
        Log.i("Longitude", (String.valueOf(mLongitude)));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        Log.i("New Provider:", "ENABLED");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("Provider:", "DISABLED");
    }
} // End of Class
