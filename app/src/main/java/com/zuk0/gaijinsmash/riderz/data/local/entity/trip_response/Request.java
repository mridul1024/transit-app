package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class Request {

    @ElementList(inline = true)
    private List<Trip> tripList;

    public List<Trip> getTripList() {
        return tripList;
    }
}
