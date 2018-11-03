package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class Etd {


    @Element(name="destination")
    private String destination;

    @Element(name="abbreviation")
    private String destinationAbbr;

    @Element(name="limited")
    private int limited;

    @ElementList(inline=true)
    private List<Estimate> estimateList; //

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationAbbr() {
        return destinationAbbr;
    }

    public void setDestinationAbbr(String destinationAbbr) {
        this.destinationAbbr = destinationAbbr;
    }

    public List<Estimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(List<Estimate> estimateList) {
        this.estimateList = estimateList;
    }

    public int getLimited() {
        return limited;
    }

    public void setLimited(int limited) {
        this.limited = limited;
    }
}
