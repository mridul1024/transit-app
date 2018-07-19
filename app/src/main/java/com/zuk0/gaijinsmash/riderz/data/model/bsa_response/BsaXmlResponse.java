package com.zuk0.gaijinsmash.riderz.data.model.bsa_response;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

@Entity( tableName = "advisories")
@Root(name="root", strict=false)
public class BsaXmlResponse {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Element
    private String date;

    @Element
    private String time;

    @Element
    private List<BSA> bsaList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BSA> getBsaList() {
        return bsaList;
    }

    public void setBsaList(List<BSA> bsaList) {
        this.bsaList = bsaList;
    }
}
