package com.example.gaijinsmash.transitapp.model.abstractClass;

import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

/**
 * Created by ryanj on 6/29/2017.
 */

public abstract class Place {

    @PrimaryKey
    private int mId;
    private String mName;
    private String mZipcode;
    private String mCity;
    private String mCounty;
    private String mState;
    private String mAddress;
    private String mLatitude;
    private String mLongitude;

    // Constructor
    public Place(String name) {
        this.mName = name;
    }

    public Place(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public Place(int id, String name, String address) {
        this.mId = id;
        this.mName = name;
        this.mAddress = address;
    }

    // Setters
    public void setId(int id) { this.mId = id; }
    public void setName(String name) {
        this.mName = name;
    }
    public void setAddress(String address) {
        this.mAddress = address;
    }
    public void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }
    public void setZipcode(String zipcode) {
        this.mZipcode = zipcode;
    }
    public void setCity(String city) {
        this.mCity = city;
    }
    public void setCounty(String county) {
        this.mCounty = county;
    }
    public void setState(String state) {
        this.mState = state;
    }

    // Getters
    public int getId() { return mId; }
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
