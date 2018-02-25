package com.example.gaijinsmash.transitapp.network;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.dialog.NetworkPermissionDialog;


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
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
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

    public Location getLocation() {
        return mLocation;
    }

    public static boolean checkGPSPermission(Context context) {
        boolean result = false;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            result = true;
        }
        return result;
    }
    public void initLocation() {
        isGPSEnabled = CheckInternet.isGPSEnabled(mContext);
        isNetworkEnabled = CheckInternet.isNetworkEnabled(mContext);
        if (!isGPSEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("GPS Settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to change that now?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
        if (!isNetworkEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Network Settings");
            alertDialog.setMessage("Network is not available. Do you want to turn it on?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
        if (isGPSEnabled && isNetworkEnabled) {
            // Define the criteria for how to select the location provider
            Criteria criteria = new Criteria();
            String locationProvider = mLocationManager.NETWORK_PROVIDER;
            if((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                mLocation = mLocationManager.getLastKnownLocation(locationProvider);
                Log.i("long", Double.toString(mLocation.getLatitude()));
            }

            // Initialize the location fields
            if(mLocation != null) {
                Log.i("GPS:", "Provider " + locationProvider + " has been selected");
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
