package com.example.weather1.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weather1.db.entity.ShortWeatherEntity
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.Weather

interface ShortWeatherDao {
    @Insert
    fun insertAll(weather: List<ShortWeatherEntity>)

    @Delete
    fun delete(weather: ShortWeatherEntity)

    @Query("SELECT * FROM shortWeather")
    fun getAll(): List<ShortWeatherEntity>
}