package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response;


import org.simpleframework.xml.Element;

public class Bsa {

    @Element(name="station", required = false)
    private String station;

    @Element(name="type", required = false)
    private String type;

    @Element(name="description", data = true)
    private String description;

    public void setStation(String station) { this.station = station; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public String getType() { return type; }
    public String getStation() { return station; }
    public String getDescription() { return description; }
}
