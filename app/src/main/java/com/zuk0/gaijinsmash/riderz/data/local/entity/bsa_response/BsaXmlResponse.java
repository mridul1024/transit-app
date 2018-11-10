package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.sql.Timestamp;
import java.util.List;

@Entity(tableName = "advisories") //todo: finish adding @ColumnInfo to fields
@Root(name="root", strict=false)
public class BsaXmlResponse {

    @PrimaryKey
    private int id;

    @ColumnInfo
    private Timestamp timestamp;

    @ColumnInfo
    @Element(name="date")
    private String date;

    @ColumnInfo
    @Element(name="time")
    private String time;

    @ColumnInfo
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
