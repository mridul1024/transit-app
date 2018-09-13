package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;

/**
 * Created by ryanj on 12/1/2017.
 */

public class Leg implements Parcelable {

    @Attribute
    private String origin;

    @Attribute
    private String destination;

    @Attribute
    private int order;

    @Attribute(name = "transfercode")
    private int transferCode;

    @Attribute
    private String line;

    @Attribute(name = "bikeflag")
    private int bikeFlag;

    @Attribute
    private String trainHeadStation;

    @Attribute
    private String origTimeMin;

    @Attribute
    private String origTimeDate;

    @Attribute
    private String destTimeMin;

    @Attribute
    private String destTimeDate;

    @Attribute(required = false)
    private String load;

    @Attribute(required = false)
    private String trainId;

    @Attribute(required = false)
    private String traindIdx;

    public void setOrigin(String origin) { this.origin = origin; }
    public String getOrigin() { return origin; }

    public void setDestination(String destination) { this.destination = destination; }
    public String getDestination() { return destination; }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    public void setTransferCode(int transferCode) {
        this.transferCode = transferCode;
    }
    public int getTransferCode() { return transferCode; }

    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public int getBikeFlag() { return bikeFlag; }
    public void setBikeFlag(int bikeFlag) { this.bikeFlag = bikeFlag; }
    public String getTrainHeadStation() { return trainHeadStation; }
    public void setTrainHeadStation(String trainHeadStation) {
        this.trainHeadStation = trainHeadStation;
    }

    public String getOrigTimeMin() { return origTimeMin; }
    public String getOrigTimeDate() { return origTimeDate; }
    public String getDestTimeMin() { return destTimeMin; }
    public String getDestTimeDate() { return destTimeDate; }

    public void setOrigTimeMin(String origTimeMin) { this.origTimeMin = origTimeMin; }
    public void setOrigTimeDate(String origTimeDate) { this.origTimeDate = origTimeDate ;}
    public void setDestTimeDate(String destTimeDate) { this.destTimeDate = destTimeDate; }
    public void setDestTimeMin(String destTimeMin) { this.destTimeMin = destTimeMin; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeInt(this.order);
        dest.writeInt(this.transferCode);
        dest.writeString(this.line);
        dest.writeInt(this.bikeFlag);
        dest.writeString(this.trainHeadStation);
        dest.writeString(this.origTimeMin);
        dest.writeString(this.origTimeDate);
        dest.writeString(this.destTimeMin);
        dest.writeString(this.destTimeDate);
        dest.writeString(this.load);
        dest.writeString(this.trainId);
        dest.writeString(this.traindIdx);
    }

    public Leg() {
    }

    private Leg(Parcel in) {
        this.origin = in.readString();
        this.destination = in.readString();
        this.order = in.readInt();
        this.transferCode = in.readInt();
        this.line = in.readString();
        this.bikeFlag = in.readInt();
        this.trainHeadStation = in.readString();
        this.origTimeMin = in.readString();
        this.origTimeDate = in.readString();
        this.destTimeMin = in.readString();
        this.destTimeDate = in.readString();
        this.load = in.readString();
        this.trainId = in.readString();
        this.traindIdx = in.readString();
    }

    public static final Parcelable.Creator<Leg> CREATOR = new Parcelable.Creator<Leg>() {
        @Override
        public Leg createFromParcel(Parcel source) {
            return new Leg(source);
        }

        @Override
        public Leg[] newArray(int size) {
            return new Leg[size];
        }
    };
}