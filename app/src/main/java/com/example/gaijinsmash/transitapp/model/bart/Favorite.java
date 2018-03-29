package com.example.gaijinsmash.transitapp.model.bart;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class Favorite {

    // title = origin + destination
    @PrimaryKey
    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "origin")
    private String origin; // abbreviated

    @ColumnInfo(name = "destination")
    private String destination; // abbreviated

    // System = BART, MUNI, etc.
    @ColumnInfo(name = "system")
    private String system;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "priority")
    private int priority;

    // Hexadecimal color codes
    @ColumnInfo(name = "color")
    private int color;

    // Getters
    public String getTitle() { return title; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getSystem() { return system; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public int getColor() { return color; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setSystem(String system) { this.system = system; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setColor(int color) { this.color = color; }
}
