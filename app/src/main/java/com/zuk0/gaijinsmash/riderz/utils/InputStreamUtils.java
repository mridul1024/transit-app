package com.zuk0.gaijinsmash.riderz.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class InputStreamUtils {

    // Attempt a URL connection and return an InputStream
    public static InputStream connectToApi(String url) throws IOException {
        URL apiURL = new URL(url);
        URLConnection connection = apiURL.openConnection();

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode != HttpURLConnection.HTTP_OK) {
            Log.wtf("InputStreamUtils", String.valueOf(responseCode));
        }

        return httpURLConnection.getInputStream();
    }
}
