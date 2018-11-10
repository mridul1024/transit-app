package com.zuk0.gaijinsmash.riderz.data.local.room.converter

import android.arch.persistence.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Fare
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Fares
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Leg
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip

import java.util.ArrayList
import java.util.HashSet

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite.Priority.OFF
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite.Priority.ON
import java.sql.Timestamp

class Converters {

    @TypeConverter
    fun toStringList(value: String?): ArrayList<String> {
        if (value.equals("") || value == null) {
            return ArrayList()
        } else {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun arrayListToString(list: ArrayList<String>?): String {
        if (list == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(list)
        }
    }

    @TypeConverter
    fun favoriteToString(favorite: Favorite) : String {
        if(false) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(favorite)
        }
    }

    @TypeConverter
    fun stringToFavorite(value: String) : Favorite {
        if(value.equals("") ) {
            return Favorite()
        } else {
            val listType = object : TypeToken<Favorite>() {}.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun toInt(priority: Favorite.Priority): Int {
        if (priority == OFF) {
            return 0
        }
        return if (priority == ON) {
            1
        } else 0
    }

    @TypeConverter
    fun toPriority(value: Int): Favorite.Priority {
        return if (value.equals(0)) {
            OFF
        } else if (value.equals(1)) {
            ON
        } else {
            throw IllegalArgumentException("Wrong argument. Value must be either 0 or 1")
        }
    }

    //todo: convert to ArraySet instead of HashSet for increased performance
    @TypeConverter
    fun toHashType(value: String?): HashSet<String> {
        if (value == "") {
            return HashSet()
        } else {
            val listType = object : TypeToken<HashSet<String>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    //todo: convert to ArraySet instead of HashSet for increased performance
    @TypeConverter
    fun hashSetToString(set: HashSet<String>?): String {
        if (set == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(set)
        }
    }

    @TypeConverter
    fun toBSAType(value: String?): Bsa {
        if (value == "") {
            return Bsa()
        } else {
            val listType = object : TypeToken<Bsa>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun bsaToString(bsa: Bsa?): String {
        if (bsa == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(bsa)
        }
    }

    @TypeConverter
    fun toBSAListType(value: String?): List<Bsa> {
        if (value == "") {
            return ArrayList()
        } else {
            val listType = object : TypeToken<List<Bsa>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun bsaListToString(bsaList: List<Bsa>?): String {
        if (bsaList == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(bsaList)
        }
    }

    @TypeConverter
    fun stationToString(station: Station?): String {
        if (station == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(station)
        }
    }

    @TypeConverter
    fun stringToStation(value: String?): Station {
        if (value == null || value == "") {
            return Station()
        } else {
            val listType = object : TypeToken<Station>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun stringToTrip(value: String?): Trip {
        if (value == null || value == "") {
            return Trip()
        } else {
            val listType = object : TypeToken<Trip>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun TripToString(trip: Trip?): String {
        if (trip == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(trip)
        }
    }

    @TypeConverter
    fun stringToFare(value: String?): Fare {
        if (value == null || value == "") {
            return Fare()
        } else {
            val listType = object : TypeToken<Fare>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fareToString(fare: Fare?): String {
        if (fare == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(fare)
        }
    }

    @TypeConverter
    fun stringToFares(value: String?): Fares {
        if (value == null || value == "") {
            return Fares()
        } else {
            val listType = object : TypeToken<Fares>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun faresToString(fares: Fares?): String {
        if (fares == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(fares)
        }
    }

    @TypeConverter
    fun stringToLeg(value: String?): Leg {
        if (value == null || value == "") {
            return Leg()
        } else {
            val listType = object : TypeToken<Leg>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun legToString(leg: Leg?): String {
        if (leg == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(leg)
        }
    }

    @TypeConverter
    fun stringToLegList(value: String?): List<Leg> {
        if (value == null || value == "") {
            return ArrayList()
        } else {
            val listType = object : TypeToken<List<Leg>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun legListToString(legList: List<Leg>?): String {
        if (legList == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(legList)
        }
    }

    @TypeConverter
    fun stringToEstimateList(value: String?): List<Estimate> {
        if (value == null || value == "") {
            return ArrayList()
        } else {
            val listType = object : TypeToken<List<Estimate>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun estimateListToString(estimateList: List<Estimate>?): String {
        if (estimateList == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(estimateList)
        }
    }

    @TypeConverter
    fun stringToFareList(value: String?): List<Fare> {
        if (value == null || value == "") {
            return ArrayList()
        } else {
            val listType = object : TypeToken<List<Fare>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fareListToString(fareList: List<Fare>?): String {
        if (fareList == null) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(fareList)
        }
    }

    @TypeConverter
    fun stringToTimestamp(value: String) : Timestamp {
        val listType = object : TypeToken<Timestamp>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun timestampToString(timestamp: Timestamp) : String {
        if(false) {
            return ""
        } else {
            val gson = Gson()
            return gson.toJson(timestamp)
        }
    }
}
