package com.zuk0.gaijinsmash.riderz.model.bart.etd_response;

import com.zuk0.gaijinsmash.riderz.model.bart.Station;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

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
