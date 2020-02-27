package com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response

import org.simpleframework.xml.Element

class Estimate {
    @field:Element(name = "origin", required = false)
    var origin: String? = null
    @field:Element(name = "destination", required = false)
    var destination: String? = null
    @field:Element(name = "minutes", required = false)
    var minutes : String? = null
    @field:Element(name = "platform", required = false)
    var platform = 0
    @field:Element(name = "direction", required = false)
    var direction: String? = null
    @field:Element(name = "length", required = false)
    var length = 0
    @field:Element(name = "color", required = false)
    var color: String? = null
    @field:Element(name = "hexcolor", required = false)
    var hexcolor: String? = null
    @field:Element(name = "bikeflag", required = false)
    var bikeflag = 0
    @field:Element(name = "delay", required = false)
    var delay = 0
    var trainHeaderStation: String? = null

}