package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Fare implements Parcelable {

    @Attribute
    private String fareAmount;

    @Attribute
    private String fareClass;

    @Attribute
    private String fareName;

    public void setFareAmount(String fareAmount) { this.fareAmount = fareAmount; }
    public void setFareClass(String fareClass) { this.fareClass = fareClass; }
    public void setFareName(String fareName) { this.fareName = fareName; }
    public String getFareAmount() { return fareAmount; }
    public String getFareClass() { return fareClass; }
    public String getFareName() { return fareName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fareAmount);
        dest.writeString(this.fareClass);
        dest.writeString(this.fareName);
    }

    public Fare() {
    }

    private Fare(Parcel in) {
        this.fareAmount = in.readString();
        this.fareClass = in.readString();
        this.fareName = in.readString();
    }

    public static final Parcelable.Creator<Fare> CREATOR = new Parcelable.Creator<Fare>() {
        @Override
        public Fare createFromParcel(Parcel source) {
            return new Fare(source);
        }

        @Override
        public Fare[] newArray(int size) {
            return new Fare[size];
        }
    };
}
