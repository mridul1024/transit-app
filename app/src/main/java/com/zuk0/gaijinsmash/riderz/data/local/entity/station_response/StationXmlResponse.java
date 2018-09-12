package com.zuk0.gaijinsmash.riderz.data.local.entity.station_response;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="root", strict=false)
public class StationXmlResponse {

    @ElementList(name = "stations")
    private List<Station> stationList;

    public List<Station> getStationList() {
        return stationList;
    }
}
