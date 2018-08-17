package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="root", strict=false)
public class EtdXmlResponse {

    @Element
    private Station station;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
