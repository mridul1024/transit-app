package com.example.gaijinsmash.transitapp.model;

import android.util.Log;

/**
 * Created by ryanj on 6/29/2017.
 */

public abstract class Place {

    private String mName;
    private String mZipcode;
    private String mCity;
    private String mCounty;
    private String mState;
    private String mAddress;
    private String mLatitude;
    private String mLongitude;

    // Constructor
    public Place() {}

    public Place(String name) {
        Log.i("Creating Place object", name);
        mName = name;
    }

    // Setters
    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public void setZipcode(String zipcode) {
        mZipcode = zipcode;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public void setCounty(String county) {
        mCounty = county;
    }

    public void setState(String state) {
        mState = state;
    }

    // Getters
    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public String getZipcode() { return mZipcode; }

    public String getCity() { return mCity; }

    public String getCounty() { return mCounty; }

    public String getState() { return mState; }


}
