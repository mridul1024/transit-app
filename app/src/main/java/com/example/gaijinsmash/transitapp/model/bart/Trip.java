package com.example.gaijinsmash.transitapp.model.bart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ryanj on 7/26/2017.
 */

public class Trip implements Parcelable {

    private String origin, destination;
    private String fare;
    private String origTimeMin, origTimeDate;
    private String destTimeMin, destTimeDate;
    private String clipper;
    private String tripTime;
    private String co2;

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getFare() { return fare; }
    public void setOrigin(String departingStation) {
        this.origin = origin;
    }
    public void setDestination(String arrivingStation) {
        this.destination = destination;
    }
    public void setFare(String fare) {
        this.fare = fare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeString(this.fare);
        dest.writeString(this.origTimeMin);
        dest.writeString(this.origTimeDate);
        dest.writeString(this.destTimeMin);
        dest.writeString(this.destTimeDate);
        dest.writeString(this.clipper);
        dest.writeString(this.tripTime);
        dest.writeString(this.co2);
    }

    public Trip() {
    }

    protected Trip(Parcel in) {
        this.origin = in.readString();
        this.destination = in.readString();
        this.fare = in.readString();
        this.origTimeMin = in.readString();
        this.origTimeDate = in.readString();
        this.destTimeMin = in.readString();
        this.destTimeDate = in.readString();
        this.clipper = in.readString();
        this.tripTime = in.readString();
        this.co2 = in.readString();
    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
