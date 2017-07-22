package com.example.gaijinsmash.transitapp.internet;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ryanj on 7/1/2017.
 */

public class InternetOperations {

    private static final boolean DEBUG = true; // True: turns on debug logging, False: off
    private static final String API_KEY = "Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    // Check for internet Connection, will return NULL if no network is currently available.
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    // Attempt a URL connection and return an InputStream
    public static InputStream connectToApi(String url) throws IOException {
        URL apiURL = new URL(url);
        URLConnection connection = apiURL.openConnection();

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode != HttpURLConnection.HTTP_OK) {
            // TODO: Handle the error gracefully

            Log.e("HTTP Connection", "Error Code: " + responseCode);
        }

        InputStream in = httpURLConnection.getInputStream();
        return in;
    }

    // TODO : Get GPS location of user

    // TODO: Find nearest Station relative to user


}
