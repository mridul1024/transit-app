package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response;

import org.simpleframework.xml.Element;

public class Estimate {

    @Element(name = "origin", required = false)
    private String origin;

    @Element(name = "destination", required = false)
    private String destination;

    @Element(name = "minutes", required = false)
    private String minutes; // can be 'leaving'

    @Element(name = "platform", required = false)
    private int platform;

    @Element(name = "direction", required = false)
    private String direction;

    @Element(name = "length", required = false)
    private int length;

    @Element(name = "color", required = false)
    private String color;

    @Element(name = "hexcolor", required = false)
    private String hexcolor;

    @Element(name = "bikeflag", required = false)
    private int bikeflag;

    @Element(name = "delay", required = false)
    private int delay;

    private String trainHeaderStation;

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

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHexcolor() {
        return hexcolor;
    }

    public void setHexcolor(String hexcolor) {
        this.hexcolor = hexcolor;
    }

    public int getBikeflag() {
        return bikeflag;
    }

    public void setBikeflag(int bikeflag) {
        this.bikeflag = bikeflag;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getTrainHeaderStation() {
        return trainHeaderStation;
    }

    public void setTrainHeaderStation(String trainHeaderStation) {
        this.trainHeaderStation = trainHeaderStation;
    }
}


