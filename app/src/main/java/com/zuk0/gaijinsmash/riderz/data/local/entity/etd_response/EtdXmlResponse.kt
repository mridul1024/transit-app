package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Entity(tableName = "estimates")
@Root(name = "root", strict = false)
class EtdXmlResponse {
    /*
      A Station object will have two ETDs
      one for  Northbound estimates and one for Southbound estimates
   */
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @field:Element
    var station: Station? = null

}