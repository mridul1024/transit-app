package com.zuk0.gaijinsmash.riderz.data.local.entity.station_response

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "root", strict = false)
class StationXmlResponse {
    @field:ElementList(name = "stations")
    var stationList: List<Station>? = null
}