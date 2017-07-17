package com.example.gaijinsmash.transitapp.model;

import android.util.Log;

/**
 * Created by ryanj on 6/29/2017.
 */

public abstract class Place {

    private String mName;
    private int mZipcode;
    private String mCity;
    private String mCounty;
    private String mState;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;

    // Constructor
    public Place() {

    }

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

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
    private void setZipcode(int zipcode) {
        mZipcode = zipcode;
    }

    private void setCity(String city) {
        mCity = city;
    }

    private void setCounty(String county) {
        mCounty = county;
    }

    private void setState(String state) {
        mState = state;
    }

    // Getters
    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getZipcode() { return mZipcode; }

    public String getCity() { return mCity; }

    public String getCounty() { return mCounty; }

    public String getState() { return mState; }


}
