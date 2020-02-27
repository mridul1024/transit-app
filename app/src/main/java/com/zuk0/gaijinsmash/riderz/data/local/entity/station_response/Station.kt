package com.zuk0.gaijinsmash.riderz.data.local.entity.station_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

@Entity(tableName = "stations")
class Station {
    // Setters
    // Getters
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "name")
    @field:Element(name = "name", required = false)
    var name: String? = null

    @ColumnInfo(name = "abbr")
    @field:Element(required = false)
    var abbr: String? = null

    @ColumnInfo(name = "latitude")
    @field:Element(name = "gtfs_latitude", required = false)
    var latitude = 0.0

    @ColumnInfo(name = "longitude")
    @field:Element(name = "gtfs_longitude", required = false)
    var longitude = 0.0

    @ColumnInfo(name = "address")
    @field:Element(name = "address", required = false)
    var address: String? = null

    @ColumnInfo(name = "city")
    @field:Element(required = false)
    var city: String? = null

    @ColumnInfo(name = "state")
    @field:Element(required = false)
    var state: String? = null

    @ColumnInfo(name = "county")
    @field:Element(required = false)
    var county: String? = null

    @ColumnInfo(name = "zipcode")
    @field:Element(required = false)
    var zipcode: String? = null

    @ColumnInfo(name = "platform_info")
    @field:Element(name = "platform_info", required = false)
    var platformInfo: String? = null

    @ColumnInfo(name = "intro")
    @field:Element(data = true, required = false)
    var intro: String? = null

    @ColumnInfo(name = "cross_street")
    @field:Element(data = true, name = "cross_street", required = false)
    var crossStreet: String? = null

    @ColumnInfo(name = "food")
    @field:Element(data = true, required = false)
    var food: String? = null

    @ColumnInfo(name = "shopping")
    @field:Element(data = true, required = false)
    var shopping: String? = null

    @ColumnInfo(name = "attraction")
    @field:Element(data = true, required = false)
    var attraction: String? = null

    @ColumnInfo(name = "link")
    @field:Element(data = true, required = false)
    var link: String? = null

    @Ignore
    @field:ElementList(name = "etd", inline = true, required = false)
    var etdList: MutableList<Etd>? = null

    @Ignore
    @field:ElementList(name = "north_routes", required = false)
    var northRoutes: MutableList<Route>? = null

    @Ignore
    @field:ElementList(name = "south_routes", required = false)
    var southRoutes: MutableList<Route>? = null

    @Ignore
    @field:ElementList(name = "north_platforms", required = false)
    var northPlatforms: MutableList<Platform>? = null

    @Ignore
    @field:ElementList(name = "south_platforms", required = false)
    var southPlatforms: MutableList<Platform>? = null

    @ColumnInfo
    @field:Element(name = "message", required = false)
    var message: String? = null

    @ColumnInfo
    @field:Element(name = "error", required = false)
    var error: String? = null

    override fun toString(): String {
        return name ?: ""
    }
}