package com.example.gaijinsmash.transitapp.model.bart;

/**
 * Created by ryanj on 12/1/2017.
 */

public class Leg {
    private String origin, destination;

    private int order;
    private int transferCode;
    private String line; //todo: how to interpret lines?
    private int bikeFlag;
    private String trainHeadStation;

    private String origTimeMin;
    private String origTimeDate;
    private String destTimeMin;
    private String destTimeDate;

    //unimportant
    private String load;
    private String trainId;
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

}
