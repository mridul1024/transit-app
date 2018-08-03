package com.zuk0.gaijinsmash.riderz.data.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;

import org.simpleframework.xml.ElementList;

import java.util.List;

@Entity(tableName = "stations")
public class Station  {


    //todo: autogenerate int key
    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "zipcode")
    private String zipcode;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "county")
    private String county;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "abbr")
    private String abbreviation;

    @ColumnInfo(name = "platform_info")
    private String platformInfo;

    @ColumnInfo(name = "intro")
    private String intro;

    @ColumnInfo(name = "cross_street")
    private String crossStreet;

    @ColumnInfo(name = "food")
    private String food;

    @ColumnInfo(name = "shopping")
    private String shopping;

    @ColumnInfo(name = "attraction")
    private String attraction;

    @ColumnInfo(name = "link")
    private String link;

    @Ignore
    @ElementList(name="etd", inline=true)
    private List<Etd> etdList;

    public Station () {}

    // Setters
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
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    // Getters
    @NonNull
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
    public String getAbbreviation() { return abbreviation; }
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

    public void setEtdList(List<Etd> etdList) {
        this.etdList = etdList;
    }
}
