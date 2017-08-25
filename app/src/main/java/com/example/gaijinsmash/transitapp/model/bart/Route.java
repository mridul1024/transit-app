package com.example.gaijinsmash.transitapp.model.bart;

/**
 * Created by ryanj on 7/26/2017.
 */

public class Route {

    //TODO: correctly assign names

    //XML TAGS
    private String origin;
    private String destination;
    private String sched_num;

    // nested data
    private String departureDate;
    private String departureTime;
    private String fare;
    private String tripTime;
    private String arrivalTime;
    private double clipper;

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
