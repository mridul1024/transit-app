package com.example.gaijinsmash.transitapp.model.bart;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "origin")
    private String origin;

    @ColumnInfo(name = "destination")
    private String destination;

    // System = BART, MUNI, etc.
    @ColumnInfo(name = "system")
    private String system;

    // Getters
    public int getId() { return id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getSystem() { return system; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setSystem(String system) { this.system = system; }
}
