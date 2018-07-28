package com.zuk0.gaijinsmash.riderz.data.local.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuk0.gaijinsmash.riderz.data.model.Favorite;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BSA;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.zuk0.gaijinsmash.riderz.data.model.Favorite.Priority.OFF;
import static com.zuk0.gaijinsmash.riderz.data.model.Favorite.Priority.ON;

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

    //todo: convert to ArraySet instead of HashSet for increased performance
    @TypeConverter
    public static HashSet<String> toHashType(String value) {
        if(value == null) {
            return new HashSet<>();
        } else {
            Type listType = new TypeToken<HashSet<String>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }
    //todo: convert to ArraySet instead of HashSet for increased performance
    @TypeConverter
    public static String hashSetToString(HashSet<String> set) {
        if(set.size() == 0) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(set);
        }
    }

    @TypeConverter
    public static BSA toBSAType(String value) {
        if(value == null) {
            return new BSA();
        } else {
            Type listType = new TypeToken<BSA>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String bsaToString(BSA bsa) {
        if(bsa == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(bsa);
        }
    }

    @TypeConverter
    public static List<BSA> toBSAListType(String value) {
        if(value == null) {
            return new ArrayList<BSA>();
        } else {
            Type listType = new TypeToken<List<BSA>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String bsaListToString(List<BSA> bsaList) {
        if(bsaList == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(bsaList);
        }
    }
}
