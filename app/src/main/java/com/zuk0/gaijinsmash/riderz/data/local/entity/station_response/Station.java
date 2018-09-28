package com.zuk0.gaijinsmash.riderz.data.local.entity.station_response;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Entity(tableName = "stations")
public class Station {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    @Element
    private String name;

    @ColumnInfo(name = "abbr")
    @Element(required = false)
    private String abbr;

    @ColumnInfo(name = "latitude")
    @Element(name = "gtfs_latitude", required = false)
    private double latitude;

    @ColumnInfo(name = "longitude")
    @Element(name = "gtfs_longitude", required = false)
    private double longitude;

    @ColumnInfo(name = "address")
    @Element(name = "address", required = false)
    private String address;

    @ColumnInfo(name = "city")
    @Element(required = false)
    private String city;

    @ColumnInfo(name = "county")
    @Element(required = false)
    private String county;

    @ColumnInfo(name = "state")
    @Element(required = false)
    private String state;

    @ColumnInfo(name = "zipcode")
    @Element(required = false)
    private String zipcode;

    @ColumnInfo(name = "platform_info")
    @Element(name = "platform_info", required = false)
    private String platformInfo;

    @ColumnInfo(name = "intro")
    @Element(data = true, required = false)
    private String intro;

    @ColumnInfo(name = "cross_street")
    @Element(data = true, name="cross_street", required = false)
    private String crossStreet;

    @ColumnInfo(name = "food")
    @Element(data = true, required = false)
    private String food;

    @ColumnInfo(name = "shopping")
    @Element(data = true, required = false)
    private String shopping;

    @ColumnInfo(name = "attraction")
    @Element(data = true, required = false)
    private String attraction;

    @ColumnInfo(name = "link")
    @Element(data = true, required = false)
    private String link;

    @Ignore
    @ElementList(name="etd", inline=true, required = false)
    private List<Etd> etdList;

    @Ignore
    @ElementList(name = "north_routes", required = false)
    private List<Route> northRoutes;

    @Ignore
    @ElementList(name = "south_routes", required = false)
    private List<Route> southRoutes;

    @Ignore
    @ElementList(name = "north_platforms", required = false)
    private List<Platform> northPlatforms;

    @Ignore
    @ElementList(name = "south_platforms", required = false)
    private List<Platform> southPlatforms;

    public Station () {}

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setCity(String city) { this.city = city; }
    public void setCounty(String county) { this.county = county; }
    public void setState(String state) { this.state = state; }
    public void setPlatformInfo(String platformInfo) { this.platformInfo = platformInfo; }
    public void setIntro(String intro) { this.intro = intro; }
    public void setCrossStreet(String crossStreet) { this.crossStreet = crossStreet; }
    public void setFood(String food) { this.food = food; }
    public void setShopping(String shopping) { this.shopping = shopping; }
    public void setAttraction(String attraction) { this.attraction = attraction; }
    public void setLink(String link) { this.link = link; }
    public void setAbbr(String abbr) { this.abbr = abbr; }
    public void setEtdList(List<Etd> etdList) {
        this.etdList = etdList;
    }
    public void setNorthRoutes(List<Route> northRoutes) {
        this.northRoutes = northRoutes;
    }
    public void setSouthRoutes(List<Route> southRoutes) {
        this.southRoutes = southRoutes;
    }


    // Getters
    @NonNull
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getZipcode() { return zipcode; }
    public String getCity() { return city; }
    public String getCounty() { return county; }
    public String getState() { return state; }
    public String getAbbr() { return abbr; }
    public String getLink() { return link; }
    public String getAttraction() { return attraction; }
    public String getShopping() { return shopping; }
    public String getFood() { return food; }
    public String getCrossStreet() { return crossStreet; }
    public String getIntro() { return intro; }
    public String getPlatformInfo() { return platformInfo; }
    public List<Etd> getEtdList() {
        return etdList;
    }
    public List<Route> getNorthRoutes() {
        return northRoutes;
    }
    public List<Route> getSouthRoutes() {
        return southRoutes;
    }

}
