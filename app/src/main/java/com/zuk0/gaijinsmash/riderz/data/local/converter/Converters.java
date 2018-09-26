package com.zuk0.gaijinsmash.riderz.data.local.converter;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Fare;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Fares;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Leg;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite.Priority.OFF;
import static com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite.Priority.ON;

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
        if(list == null) {
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
        if(set == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(set);
        }
    }

    @TypeConverter
    public static Bsa toBSAType(String value) {
        if(value == null) {
            return new Bsa();
        } else {
            Type listType = new TypeToken<Bsa>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String bsaToString(Bsa bsa) {
        if(bsa == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(bsa);
        }
    }

    @TypeConverter
    public static List<Bsa> toBSAListType(String value) {
        if(value == null) {
            return new ArrayList<Bsa>();
        } else {
            Type listType = new TypeToken<List<Bsa>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String bsaListToString(List<Bsa> bsaList) {
        if(bsaList == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(bsaList);
        }
    }

    @TypeConverter
    public static String stationToString(Station station) {
        if(station == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(station);
        }
    }

    @TypeConverter
    public static Station stringToStation(String value) {
        if(value == null || value.equals("")) {
            return new Station();
        } else {
            Type listType = new TypeToken<Station>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static Trip stringToTrip(String value) {
        if(value == null || value.equals("")) {
            return new Trip();
        } else {
            Type listType = new TypeToken<Trip>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String TripToString(Trip trip) {
        if(trip == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(trip);
        }
    }

    @TypeConverter
    public static Fare stringToFare(String value) {
        if(value == null || value.equals("")) {
            return new Fare();
        } else {
            Type listType = new TypeToken<Fare>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String fareToString(Fare fare) {
        if(fare == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(fare);
        }
    }

    @TypeConverter
    public static Fares stringToFares(String value) {
        if(value == null || value.equals("")) {
            return new Fares();
        } else {
            Type listType = new TypeToken<Fares>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String faresToString(Fares fares) {
        if(fares == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(fares);
        }
    }

    @TypeConverter
    public static Leg stringToLeg(String value) {
        if(value == null || value.equals("")) {
            return new Leg();
        } else {
            Type listType = new TypeToken<Leg>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String legToString(Leg leg) {
        if(leg == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(leg);
        }
    }

    @TypeConverter
    public static List<Leg> stringToLegList(String value) {
        if(value == null || value.equals("")) {
            return new ArrayList<>();
        } else {
            Type listType = new TypeToken<List<Leg>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String legListToString(List<Leg> legList) {
        if(legList == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(legList);
        }
    }

    @TypeConverter
    public static List<Estimate> stringToEstimateList(String value) {
        if(value == null || value.equals("")){
            return new ArrayList<>();
        } else {
            Type listType = new TypeToken<List<Estimate>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String estimateListToString(List<Estimate> estimateList) {
        if(estimateList == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(estimateList);
        }
    }

    @TypeConverter
    public static List<Fare> stringToFareList(String value) {
        if(value == null || value.equals("")){
            return new ArrayList<>();
        } else {
            Type listType = new TypeToken<List<Fare>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
    }

    @TypeConverter
    public static String fareListToString(List<Fare> fareList) {
        if(fareList == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(fareList);
        }
    }

}
