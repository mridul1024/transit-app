package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

class Etd {
    @field:Element(name = "destination")
    var destination: String? = null
    @field:Element(name = "abbreviation")
    var destinationAbbr: String? = null
    @field:Element(name = "limited")
    var limited = 0
    @field:ElementList(inline = true)
    var estimateList : MutableList<Estimate>? = null

}