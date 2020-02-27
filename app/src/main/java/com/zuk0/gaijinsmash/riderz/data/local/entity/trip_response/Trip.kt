package com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "trips")
class Trip {
    /*
     NOTE: origin and destination will return the Abbreviation of a Station Name,
     because of the way BART API returns results.
     You will need to convert them to their full names
    */
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @SerializedName("@origin")
    @Expose
    var origin: String? = null
    @SerializedName("@destination")
    @Expose
    var destination: String? = null
    @SerializedName("@fare")
    @Expose
    var fare: String? = null
    @SerializedName("@origTimeMin")
    @Expose
    var origTimeMin: String? = null
    @SerializedName("@origTimeDate")
    @Expose
    var origTimeDate: String? = null
    @SerializedName("@destTimeMin")
    @Expose
    var destTimeMin: String? = null
    @SerializedName("@destTimeDate")
    @Expose
    var destTimeDate: String? = null
    //todo: deprecated
    @SerializedName("@clipper")
    @Expose
    var clipper: String? = null
    @SerializedName("@tripTime")
    @Expose
    var tripTime: String? = null
    //todo: deprecated
    @SerializedName("@co2")
    @Expose
    var co2: String? = null
    //todo: deprecated to only cash fares.
    @SerializedName("fares")
    @Expose
    var fares: Fares? = null
    @SerializedName("leg")
    @Expose
    var legList: List<Leg>? = null

}