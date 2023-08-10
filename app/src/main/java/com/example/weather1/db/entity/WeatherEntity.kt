package com.example.weather1.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.weather1.network.Clouds
import com.example.weather1.network.Coord
import com.example.weather1.network.Main
import com.example.weather1.network.Sys
import com.example.weather1.network.Weather
import com.example.weather1.network.Wind

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    @Embedded
    var coord: Coord? = Coord(),
    @Relation(
        entity = ShortWeatherEntity::class,
        parentColumn = "id",
        entityColumn = "weatherId"
    )
    val weather: List<Weather> = listOf(),
    val base: String? = null,
    @Embedded
    val main: Main? = Main(),
    val visibility: Int? = null,
    @Embedded
    val wind: Wind? = Wind(),
    @Embedded
    val clouds: Clouds? = Clouds(),
    val dt: Int? = null,
    @Embedded
    val sys: Sys? = Sys(),
    val timezone: Int? = null,
    val name: String? = null,
    val cod: Int? = null
)