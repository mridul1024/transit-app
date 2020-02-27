package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "bsa", strict = false)
data class Bsa
@JvmOverloads constructor(

    //@Attribute(required = false)
    @param:Element(name = "id", required = false)
    @field:Element(name = "id", required = false)
    var id: Int? = 0,

    @Expose
    @SerializedName("station")
   // @param:Element(name = "station", required = false)
    @field:Element(name = "station", required = false)
    var station: Station? = null,

    @Expose
    @SerializedName("type")
   // @param:Element(name = "type", required = false)
    @field:Element(name = "type", required = false)
    var type: String? = null,

    @Expose
    @SerializedName("description")
    //@param:Element(name = "description", data = true, required = false)
    @field:Element(name = "description", data = true, required = false)
    var description: String? = null,

    @Expose
    @SerializedName("sms_text")
    //@param:Element(name = "sms_text", required = false)
    @field:Element(name = "sms_text", required = false)
    var sms_text: String? = null,

    @Expose
    @SerializedName("posted")
    //@param:Element(name = "posted", required = false)
    @field:Element(name = "posted", required = false)
    var posted: String? = null,

    @Expose
    @SerializedName("expires")
    //@param:Element(name = "expires", required = false)
    @field:Element(name = "expires", required = false)
    var expires: String? = null

)