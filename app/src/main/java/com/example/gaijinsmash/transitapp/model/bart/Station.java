package com.example.gaijinsmash.transitapp.model.bart;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.gaijinsmash.transitapp.model.abstractClass.Place;

@Entity(tableName = "stations")
public class Station  {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "zipcode")
    private String zipcode;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "county")
    private String county;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "abbr")
    private String abbreviation;

    @ColumnInfo(name = "platform_info")
    private String platformInfo;

    @ColumnInfo(name = "intro")
    private String intro;

    @ColumnInfo(name = "cross_street")
    private String crossStreet;

    @ColumnInfo(name = "food")
    private String food;

    @ColumnInfo(name = "shopping")
    private String shopping;

    @ColumnInfo(name = "attraction")
    private String attraction;

    @ColumnInfo(name = "link")
    private String link;



    public Station(String name) {
        this.name = name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public String getAbbreviation() { return abbreviation; }

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
