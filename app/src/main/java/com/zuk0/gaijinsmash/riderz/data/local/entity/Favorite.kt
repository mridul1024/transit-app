package com.zuk0.gaijinsmash.riderz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import com.zuk0.gaijinsmash.riderz.data.local.room.converter.Converters

import java.util.ArrayList
import java.util.HashSet


@Entity(tableName = "favorites")
class Favorite {

    // Getters
    // Setters
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "a")
    lateinit var a: Station

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "b")
    lateinit var b: Station

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "trainHeaderStations") //abbreviated
    lateinit var trainHeaderStations: ArrayList<String>

    // todo: convert to enum: System = BART, MUNI, etc.
    @ColumnInfo(name = "system")
    lateinit var system: String

    @ColumnInfo(name = "description")
    var description: String? = ""

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "priority")
    var priority = Priority.OFF //default

    // Hexadecimal color codes
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "colors")
    var colors: HashSet<String>? = null

    enum class Priority constructor(val value: Int) {
        ON(1), OFF(0)
    }

}
