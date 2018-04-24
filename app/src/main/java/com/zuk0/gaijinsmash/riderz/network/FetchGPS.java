package com.zuk0.gaijinsmash.riderz.network;

import android.Manifest;
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
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class FetchGPS implements LocationListener {

    private final Context mContext;
    private LocationManager mLocationManager;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;

    public FetchGPS(Context context) {
        this.mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria for how to select the location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String locationProvider = mLocationManager.getBestProvider(criteria, true);

        if((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mLocation = mLocationManager.getLastKnownLocation(locationProvider);
        }

        // Initialize the location fields
        if(mLocation != null) {
            onLocationChanged(mLocation);
        } else {
            Log.i("GPS:", "Location is not available");
        }
        //initLocation();
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
        boolean isGPSEnabled = CheckInternet.isGPSEnabled(mContext);
        boolean isNetworkEnabled = CheckInternet.isNetworkEnabled(mContext);
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
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            if((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                mLocation = mLocationManager.getLastKnownLocation(locationProvider);
            }

            // Initialize the location fields
            if(mLocation != null) {
                onLocationChanged(mLocation);
            } else {
                Log.i("GPS:", "Location is not available");
            }
        }
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
