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

public class FetchInputStream {

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
}
