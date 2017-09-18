package com.example.gaijinsmash.transitapp.internet;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by ryanj on 9/17/2017.
 */

public class FetchLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
