package com.example.weather1.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "shortWeather",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["id"],
        childColumns = ["weatherId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ShortWeatherEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val weatherId: Long,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null

)