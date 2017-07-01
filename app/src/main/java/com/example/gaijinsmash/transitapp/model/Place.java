package com.example.gaijinsmash.transitapp.model;

import android.util.Log;

/**
 * Created by ryanj on 6/29/2017.
 */

public abstract class Place {

    private String name;
    private int zipcode;
    private String city;
    private String county;
    private String state;
    private String address;
    private double latitude;
    private double longitude;

    // Constructor
    public Place(String name) {
        Log.i("Creating Place object", name);
        this.name = name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    private void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void setCounty(String county) {
        this.county = county;
    }

    private void setState(String state) {
        this.state = state;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getZipcode() { return zipcode; }

    public String getCity() { return city; }

    public String getCounty() { return county; }

    public String getState() { return state; }


}
