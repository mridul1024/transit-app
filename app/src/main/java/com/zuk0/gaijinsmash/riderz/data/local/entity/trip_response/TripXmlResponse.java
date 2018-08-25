package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import android.arch.persistence.room.ColumnInfo;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "root")
public class TripXmlResponse {

    @ElementList
    private List<Trip> tripList;

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }
}
