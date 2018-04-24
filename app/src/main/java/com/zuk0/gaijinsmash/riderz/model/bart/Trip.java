package com.zuk0.gaijinsmash.riderz.model.bart;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {


    // NOTE: origin and destination will return the Abbreviation of a Station.
    //          Will need to convert to the full name.
    private String origin;
    private String destination;
    private String fare;
    private String origTimeMin;
    private String origTimeDate;
    private String destTimeMin;
    private String destTimeDate;
    private String clipper;
    private String tripTime;
    private String co2;

    public Trip() { }

    protected Trip(Parcel in) {
        origin = in.readString();
        destination = in.readString();
        fare = in.readString();
        origTimeMin = in.readString();
        origTimeDate = in.readString();
        destTimeMin = in.readString();
        destTimeDate = in.readString();
        clipper = in.readString();
        tripTime = in.readString();
        co2 = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getFare() { return fare; }
    public String getOrigTimeMin() { return origTimeMin; }
    public String getOrigTimeDate() { return origTimeDate; }
    public String getDestTimeMin() { return destTimeMin; }
    public String getDestTimeDate() { return destTimeDate; }
    public String getClipper() { return clipper; }
    public String getTripTime() { return tripTime; }
    public String getCo2() { return co2; }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setFare(String fare) {
        this.fare = fare;
    }
    public void setOrigTimeMin(String origTimeMin) { this.origTimeMin = origTimeMin; }
    public void setOrigTimeDate(String origTimeDate) { this.origTimeDate = origTimeDate; }
    public void setDestTimeMin(String destTimeMin) { this.destTimeMin = destTimeMin; }
    public void setDestTimeDate(String destTimeDate) { this.destTimeDate = destTimeDate; }
    public void setClipper(String clipper) { this.clipper = clipper; }
    public void setTripTime(String tripTime) { this.tripTime = tripTime; }
    public void setCo2(String co2) { this.co2 = co2; }

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
}
