package com.example.gaijinsmash.transitapp.model.bart;


public class Advisory {

    private String date;
    private String time;

    //BSA
    private int id;
    private String station;
    private String type;
    private String description;

    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setId(int id) { this.id = id; }
    public void setStation(String station) { this.station = station; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getDescription() { return description; }
}
