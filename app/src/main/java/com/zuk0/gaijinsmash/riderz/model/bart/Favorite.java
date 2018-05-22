package com.zuk0.gaijinsmash.riderz.model.bart;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.zuk0.gaijinsmash.riderz.database.Converters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "origin")
    private String origin; // abbreviated

    @ColumnInfo(name = "destination")
    private String destination; // abbreviated

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
}
