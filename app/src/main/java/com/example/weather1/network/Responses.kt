package com.example.weather1.network

import com.squareup.moshi.Json

data class RespCurrentWeather(
    @Json(name = "coord") var coord: Coord? = Coord(),
    val weather: List<Weather> = listOf(),
    val base: String? = null,
    val main: Main? = Main(),
    val visibility: Int? = null,
    val wind: Wind? = Wind(),
    val clouds: Clouds? = Clouds(),
    val dt: Int? = null,
    val sys: Sys? = Sys(),
    val timezone: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val cod: Int? = null
)

data class Coord(

    val lon: Double? = null,
    val lat: Double? = null

)

data class Weather(

    var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null

)

data class Main(

    var temp: Double? = null,
    @Json(name = "feels_like")var feelsLike: Double? = null,
    @Json(name = "temp_min")var tempMin: Double? = null,
    @Json(name = "temp_max")var tempMax: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    @Json(name = "sea_level")var seaLevel: Int? = null,
    @Json(name = "grnd_level")var grndLevel: Int? = null

)

data class Wind(

    var speed: Double? = null,
    var deg: Int? = null,
    var gust: Double? = null

)

data class Clouds(

    var all: Int? = null

)

data class Sys(

    var country: String? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null

)


