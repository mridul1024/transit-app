package com.example.gaijinsmash.transitapp.model;

/**
 * Created by ryanj on 6/29/2017.
 */

public class Station extends Place {

    private String abbreviation;

    // Constructor
    public Station(String name) {
        super(name);
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() { return abbreviation; }
}
