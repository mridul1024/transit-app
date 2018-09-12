package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//todo: prepare to delete this
public class FullTrip implements Parcelable {

    private Trip trip;
    private List<Fare> fareList;
    private List<Leg> legList;

    public Trip getTrip() { return trip; }
    public List<Fare> getFareList() { return fareList; }
    public List<Leg> getLegList() { return legList; }

    public void setTrip(Trip trip) { this.trip = trip; }
    public void setFareList(List<Fare> fareList) { this.fareList = fareList; }
    public void setLegList(List<Leg> legList) { this.legList = legList; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.trip, flags);
        dest.writeList(this.fareList);
        dest.writeList(this.legList);
    }

    public FullTrip() {
    }

    private FullTrip(Parcel in) {
        this.trip = in.readParcelable(Trip.class.getClassLoader());
        this.fareList = new ArrayList<>();
        in.readList(this.fareList, Fare.class.getClassLoader());
        this.legList = new ArrayList<>();
        in.readList(this.legList, Leg.class.getClassLoader());
    }

    public static final Parcelable.Creator<FullTrip> CREATOR = new Parcelable.Creator<FullTrip>() {
        @Override
        public FullTrip createFromParcel(Parcel source) {
            return new FullTrip(source);
        }

        @Override
        public FullTrip[] newArray(int size) {
            return new FullTrip[size];
        }
    };
}

