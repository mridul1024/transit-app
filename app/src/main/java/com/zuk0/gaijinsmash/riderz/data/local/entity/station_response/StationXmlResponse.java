package com.zuk0.gaijinsmash.riderz.data.local.entity.station_response;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="root", strict=false)
public class StationXmlResponse {

    @ElementList(name="stations")
    List<Station> stationList;

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }
}
