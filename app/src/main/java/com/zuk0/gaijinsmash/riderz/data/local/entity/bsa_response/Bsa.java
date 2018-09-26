package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Bsa {

    @Attribute
    private int id;

    @Element(name="station", required = false)
    private String station;

    @Element(name="type", required = false)
    private String type;

    @Element(name="description", data = true)
    private String description;

    @Element(required = false)
    private String sms_text;

    @Element(required = false)
    private String posted;

    @Element(required = false)
    private String expires;

    public void setStation(String station) { this.station = station; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public String getType() { return type; }
    public String getStation() { return station; }
    public String getDescription() { return description; }

    public String getSms_text() {
        return sms_text;
    }

    public void setSms_text(String sms_text) {
        this.sms_text = sms_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
