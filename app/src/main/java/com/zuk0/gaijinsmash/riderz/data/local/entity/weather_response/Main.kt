package com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response

data class Main (
        var temp: Double? = null,
        var pressure: Int? = null,
        var humidity: Int? = null,
        var temp_min: Double? = null,
        var temp_max: Double? = null
)