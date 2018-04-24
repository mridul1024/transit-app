package com.zuk0.gaijinsmash.riderz.network;

import android.content.Context;

import com.zuk0.gaijinsmash.riderz.utils.ErrorToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FetchInputStream {
    private Context mContext;

    public FetchInputStream(Context mContext) {
        this.mContext = mContext;
    }

    // Attempt a URL connection and return an InputStream
    public InputStream connectToApi(String url) throws IOException {
        URL apiURL = new URL(url);
        URLConnection connection = apiURL.openConnection();

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode != HttpURLConnection.HTTP_OK) {
            ErrorToast.networkConnectionErrorToast(mContext, responseCode);
        }

        return httpURLConnection.getInputStream();
    }
}
