package com.zuk0.gaijinsmash.riderz.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.zuk0.gaijinsmash.riderz.model.bart.Favorite.Priority.OFF;
import static com.zuk0.gaijinsmash.riderz.model.bart.Favorite.Priority.ON;

public class Converters {

    @TypeConverter
    public static ArrayList<String> toStringList(String value) {
        if(value ==null) {
            return new ArrayList<String>();
        } else {
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String arrayListToString(ArrayList<String> list) {
        if(list.size() == 0) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(list);
        }
    }

    @TypeConverter
    public static int toInt(Favorite.Priority priority) {
        if(priority == OFF) {
            return 0;
        }
        if(priority == ON) {
            return 1;
        }
        return 0;
    }

    @TypeConverter
    public static Favorite.Priority toPriority(int value) {
        if(value == 0) {
            return OFF;
        } else if (value == 1) {
            return ON;
        } else {
            throw new IllegalArgumentException("Wrong argument. Value must be either 0 or 1");
        }
    }

    @TypeConverter
    public static HashSet<String> toHashType(String value) {
        if(value == null) {
            return new HashSet<>();
        } else {
            Type listType = new TypeToken<HashSet<String>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String hashSetToString(HashSet<String> set) {
        if(set.size() == 0) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(set);
        }
    }
}