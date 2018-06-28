package com.zuk0.gaijinsmash.riderz.model.bart.etd_response;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class EstimateList {

    @ElementList(name="root", inline=true)
    private List<Estimate> estimateList;

    public List<Estimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(List<Estimate> estimateList) {
        this.estimateList = estimateList;
    }
}