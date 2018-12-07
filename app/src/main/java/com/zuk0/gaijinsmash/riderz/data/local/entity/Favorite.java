package com.zuk0.gaijinsmash.riderz.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters;

import java.util.ArrayList;
import java.util.HashSet;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "origin")
    private String origin; // abbreviated

    @ColumnInfo(name = "destination")
    private String destination; // abbreviated

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "trainHeaderStations") //abbreviated
    private ArrayList<String> trainHeaderStations;

    // todo: convert to enum: System = BART, MUNI, etc.
    @ColumnInfo(name = "system")
    private String system;

    @ColumnInfo(name = "description")
    private String description;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "priority")
    private Priority priority;

    // Hexadecimal color codes
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "colors")
    private HashSet<String> colors;


    public enum Priority {
        ON(1), OFF(0);

        private int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() { return value; }
    }

    public Favorite() {}

    // Getters
    public int getId() { return id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getSystem() { return system; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public HashSet<String> getColors() {
        return colors;
    }
    public ArrayList<String> getTrainHeaderStations() { return trainHeaderStations; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setSystem(String system) { this.system = system; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setColors(HashSet<String> colors) {
        this.colors = colors;
    }
    public void setTrainHeaderStations(ArrayList<String> trainHeaderStations) { this.trainHeaderStations = trainHeaderStations; }

}
