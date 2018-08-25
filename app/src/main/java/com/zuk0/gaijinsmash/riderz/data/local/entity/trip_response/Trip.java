package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.os.Parcel;
import android.os.Parcelable;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


public class Trip implements Parcelable {

    // NOTE: origin and destination will return the Abbreviation of a Station,
    // because of the way BART API returns results.
    // Will need to convert to the full name.

    @Attribute
    private String origin;

    @Attribute
    private String destination;

    @Attribute
    private String origTimeMin;

    @Attribute
    private String origTimeDate;

    @Attribute
    private String destTimeMin;

    @Attribute
    private String destTimeDate;

    @Attribute
    private String tripTime;

    @Element
    private Fare fare;

    @ElementList
    private List<Leg> legList; //todo: check this

    // only used for real time estimates
    private List<Estimate> estimateList;
    private String destinationAbbr;

    public Trip() { }

    //todo: add fare object to parcelable?
    private Trip(Parcel in) {
        origin = in.readString();
        destination = in.readString();
        origTimeMin = in.readString();
        origTimeDate = in.readString();
        destTimeMin = in.readString();
        destTimeDate = in.readString();
        tripTime = in.readString();
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
    public String getOrigTimeMin() { return origTimeMin; }
    public String getOrigTimeDate() { return origTimeDate; }
    public String getDestTimeMin() { return destTimeMin; }
    public String getDestTimeDate() { return destTimeDate; }
    public String getTripTime() { return tripTime; }
    public List<Estimate> getEstimateList() {
        return estimateList;
    }
    public String getDestinationAbbr() { return  destinationAbbr; }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public List<Leg> getLegList() {
        return legList;
    }

    public void setLegList(List<Leg> legList) {
        this.legList = legList;
    }

    public void setEstimateList(List<Estimate> estimateList) {
        this.estimateList = estimateList;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setOrigTimeMin(String origTimeMin) { this.origTimeMin = origTimeMin; }
    public void setOrigTimeDate(String origTimeDate) { this.origTimeDate = origTimeDate; }
    public void setDestTimeMin(String destTimeMin) { this.destTimeMin = destTimeMin; }
    public void setDestTimeDate(String destTimeDate) { this.destTimeDate = destTimeDate; }
    public void setTripTime(String tripTime) { this.tripTime = tripTime; }
    public void setDestinationAbbr(String abbr) { this.destinationAbbr = abbr; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeString(this.origTimeMin);
        dest.writeString(this.origTimeDate);
        dest.writeString(this.destTimeMin);
        dest.writeString(this.destTimeDate);
        dest.writeString(this.tripTime);
    }

}
