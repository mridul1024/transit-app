package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "root", strict = false)
public class TripXmlResponse {

    @Element
    private String origin;

    @Element
    private String destination;

    @Element
    private String sched_num;

    @Element
    private Schedule schedule;

    public Schedule getSchedule() { return schedule; }

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

    public String getSched_num() {
        return sched_num;
    }

    public void setSched_num(String sched_num) {
        this.sched_num = sched_num;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}

