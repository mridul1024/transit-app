package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Entity(tableName = "advisories") //todo: finish adding @ColumnInfo to fields
@Root(name="root", strict=false)
public class BsaXmlResponse {

    @PrimaryKey
    private int id;

    @Element(name="date")
    private String date;

    @Element(name="time")
    private String time;

    @ElementList(inline = true)
    private List<Bsa> bsaList;

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

    public List<Bsa> getBsaList() {
        return bsaList;
    }

    public void setBsaList(List<Bsa> bsaList) {
        this.bsaList = bsaList;
    }
}
