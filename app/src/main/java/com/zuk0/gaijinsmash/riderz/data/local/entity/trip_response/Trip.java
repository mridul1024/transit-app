package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trips")
public class Trip implements Parcelable {

    // NOTE: origin and destination will return the Abbreviation of a Station,
    // because of the way BART API returns results.
    // You will need to convert them to their full names

    @PrimaryKey(autoGenerate = true)
    private int id;

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

    @Attribute
    private String fare;

    @ElementList(name = "fares", required = false)
    private List<Fare> fareList;

    @ElementList(inline = true)
    private List<Leg> legList; //todo: check this

    // only used for real time estimates
    @ElementList(required = false)
    private List<Estimate> estimateList;

    public Trip() { }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

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
    public List<Leg> getLegList() {
        return legList;
    }
    public List<Fare> getFareList() {
        return fareList;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFareList(List<Fare> fareList) {
        this.fareList = fareList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeString(this.origTimeMin);
        dest.writeString(this.origTimeDate);
        dest.writeString(this.destTimeMin);
        dest.writeString(this.destTimeDate);
        dest.writeString(this.tripTime);
        dest.writeString(this.fare);
        dest.writeTypedList(this.fareList);
        dest.writeTypedList(this.legList);
        dest.writeList(this.estimateList);
    }

    protected Trip(Parcel in) {
        this.id = in.readInt();
        this.origin = in.readString();
        this.destination = in.readString();
        this.origTimeMin = in.readString();
        this.origTimeDate = in.readString();
        this.destTimeMin = in.readString();
        this.destTimeDate = in.readString();
        this.tripTime = in.readString();
        this.fare = in.readString();
        this.fareList = in.createTypedArrayList(Fare.CREATOR);
        this.legList = in.createTypedArrayList(Leg.CREATOR);
        this.estimateList = new ArrayList<Estimate>();
        in.readList(this.estimateList, Estimate.class.getClassLoader());
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
