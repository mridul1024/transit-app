package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import org.simpleframework.xml.Element;

public class Schedule {

    @Element
    String date;

    @Element
    String time;

    @Element
    private Request request;

    public Request getRequest() { return request; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}