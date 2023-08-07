package com.example.weather1.network

import com.squareup.moshi.Json

data class RespCurrentWeather(
    @Json(name = "coord") var coord      : Coord?             = Coord(),
    val weather    : List<Weather> = listOf(),
    val base       : String?            = null,
    val main       : Main?              = Main(),
    val visibility : Int?               = null,
    val wind       : Wind?              = Wind(),
    val clouds     : Clouds?            = Clouds(),
    val dt         : Int?               = null,
    val sys        : Sys?               = Sys(),
    val timezone   : Int?               = null,
    val id         : Int?               = null,
    val name       : String?            = null,
    val cod        : Int?               = null
)

data class Coord (

    val lon : Double? = null,
    val lat : Double? = null

)