package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uri {

    @SerializedName("#cdata-section")
    @Expose
    private String cdataSection;

    public String getCdataSection() {
        return cdataSection;
    }

    public void setCdataSection(String cdataSection) {
        this.cdataSection = cdataSection;
    }

}