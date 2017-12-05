package com.example.gaijinsmash.transitapp.model.bart;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

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

    public String getTwentyFourHr() {
        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String output = null;
        try {
            date = df.parse(time);
            output = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }
}
