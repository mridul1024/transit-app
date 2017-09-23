package com.example.gaijinsmash.transitapp.model.bart;

import com.example.gaijinsmash.transitapp.model.abstractClass.Place;

/**
 * Created by ryanj on 6/29/2017.
 */

public class Station extends Place {

    private String mAbbreviation;

    public Station() {}

    public Station(String name) {
        super(name);
    }

    public void setAbbreviation(String abbreviation) {
        mAbbreviation = abbreviation;
    }

    public String getAbbreviation() { return mAbbreviation; }
}
