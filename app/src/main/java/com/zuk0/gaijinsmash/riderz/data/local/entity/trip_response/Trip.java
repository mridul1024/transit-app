package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "trips")
public class Trip  {

    /*
     NOTE: origin and destination will return the Abbreviation of a Station Name,
     because of the way BART API returns results.
     You will need to convert them to their full names
    */

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("@origin")
    @Expose
    private String origin;

    @SerializedName("@destination")
    @Expose
    private String destination;

    @SerializedName("@fare")
    @Expose
    private String fare;

    @SerializedName("@origTimeMin")
    @Expose
    private String origTimeMin;

    @SerializedName("@origTimeDate")
    @Expose
    private String origTimeDate;

    @SerializedName("@destTimeMin")
    @Expose
    private String destTimeMin;

    @SerializedName("@destTimeDate")
    @Expose
    private String destTimeDate;

    @SerializedName("@clipper")
    @Expose
    private String clipper;

    @SerializedName("@tripTime")
    @Expose
    private String tripTime;

    @SerializedName("@co2")
    @Expose
    private String co2;

    @SerializedName("fares")
    @Expose
    private Fares fares;

    @SerializedName("leg")
    @Expose
    private List<Leg> legList = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getOrigTimeMin() {
        return origTimeMin;
    }

    public void setOrigTimeMin(String origTimeMin) {
        this.origTimeMin = origTimeMin;
    }

    public String getOrigTimeDate() {
        return origTimeDate;
    }

    public void setOrigTimeDate(String origTimeDate) {
        this.origTimeDate = origTimeDate;
    }

    public String getDestTimeMin() {
        return destTimeMin;
    }

    public void setDestTimeMin(String destTimeMin) {
        this.destTimeMin = destTimeMin;
    }

    public String getDestTimeDate() {
        return destTimeDate;
    }

    public void setDestTimeDate(String destTimeDate) {
        this.destTimeDate = destTimeDate;
    }

    public String getClipper() {
        return clipper;
    }

    public void setClipper(String clipper) {
        this.clipper = clipper;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public Fares getFares() {
        return fares;
    }

    public void setFares(Fares fares) {
        this.fares = fares;
    }

    public List<Leg> getLegList() {
        return legList;
    }

    public void setLegList(List<Leg> legList) {
        this.legList = legList;
    }
}
