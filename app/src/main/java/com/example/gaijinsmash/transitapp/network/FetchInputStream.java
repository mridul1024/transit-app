package com.example.gaijinsmash.transitapp.network;

import android.content.Context;

import com.example.gaijinsmash.transitapp.utils.ErrorToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ryanj on 7/1/2017.
 */

public class FetchInputStream {
    private Context mContext;

    public FetchInputStream(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

    // Attempt a URL connection and return an InputStream
    public InputStream connectToApi(String url) throws IOException {
        URL apiURL = new URL(url);
        URLConnection connection = apiURL.openConnection();

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode != HttpURLConnection.HTTP_OK) {
            ErrorToast toast = new ErrorToast();
            toast.networkConnectionErrorToast(getContext(), responseCode);
        }

        InputStream in = httpURLConnection.getInputStream();
        return in;
    }
}
