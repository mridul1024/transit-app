package com.example.gaijinsmash.transitapp.model.bart;

import android.arch.persistence.room.Entity;

import com.example.gaijinsmash.transitapp.model.abstractClass.Place;

@Entity // Connects to Room - sqlite
public class Station extends Place {

    private String mAbbreviation;

    public Station(String name) {
        super(name);
    }

    public Station(int id, String name) {
        super(id, name);
    }

    public Station(int id, String name, String address) {
        super(id, name ,address);
    }

    public void setAbbreviation(String abbreviation) {
        mAbbreviation = abbreviation;
    }

    public String getAbbreviation() { return mAbbreviation; }
}
