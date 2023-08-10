package com.example.weather1.helpers

import com.example.weather1.db.entity.ShortWeatherEntity
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.RespCurrentWeather
import com.example.weather1.network.Weather

fun RespCurrentWeather.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        id = id?.toLong() ?: 0,
        coord = coord,
        weather = weather,
        base = base,
        main = main,
        visibility = visibility,
        wind = wind,
        clouds = clouds,
        dt = dt,
        sys = sys,
        timezone = timezone,
        name = name,
        cod = cod
    )
}

fun Weather.toShortWeatherEntity(weatherId: Long): ShortWeatherEntity {
    return ShortWeatherEntity(
        id = id?.toLong() ?: 0,
        weatherId = weatherId,
        main = main,
        description = description,
        icon = icon
    )
}