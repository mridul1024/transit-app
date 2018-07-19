package com.zuk0.gaijinsmash.riderz.data.model.bsa_response;


import org.simpleframework.xml.Element;

public class BSA {

    @Element(name="station")
    private String station;

    @Element(name="type")
    private String type;

    @Element(name="description")
    private String description;

    public void setStation(String station) { this.station = station; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }

    public String getStation() { return station; }
    public String getType() { return type; }
    public String getDescription() { return description; }
}
