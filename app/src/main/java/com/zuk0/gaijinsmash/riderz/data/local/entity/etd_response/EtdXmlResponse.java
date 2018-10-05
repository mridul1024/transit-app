package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Entity(tableName = "estimates")
@Root(name="root", strict=false)
public class EtdXmlResponse {

    /*
      A Station object will have two ETDs
      one for  Northbound estimates and one for Southbound estimates
   */
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Element
    private Station station;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
