package com.example.gaijinsmash.transitapp.internet;

import android.content.Context;
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
}
