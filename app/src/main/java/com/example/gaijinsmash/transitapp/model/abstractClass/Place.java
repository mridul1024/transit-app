package com.example.gaijinsmash.transitapp.model.abstractClass;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public abstract class Place {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "zipcode")
    private String zipcode;

    @NonNull
    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "county")
    private String county;

    @ColumnInfo(name = "state")
    private String state;

    @NonNull
    @ColumnInfo(name = "address")
    private String address;

    @NonNull
    @ColumnInfo(name = "latitude")
    private double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private double longitude;

    // Constructor
    public Place(String name) {
        this.name = name;
    }

    // Setters
    public void setId(int id) { this.id = id; }
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
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public void setState(String state) {
        this.state = state;
    }

    // Getters
    public int getId() { return id; }
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
    public String getZipcode() { return zipcode; }
    public String getCity() { return city; }
    public String getCounty() { return county; }
    public String getState() { return state; }
}
