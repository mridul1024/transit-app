package com.zuk0.gaijinsmash.riderz.data.model.etd_response;

import com.zuk0.gaijinsmash.riderz.data.model.Station;

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
