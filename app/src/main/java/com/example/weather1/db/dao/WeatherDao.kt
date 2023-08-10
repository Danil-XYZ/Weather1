package com.example.weather1.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.Weather

@Dao
interface WeatherDao {
    @Insert
    fun insertAll(weather: WeatherEntity)

    @Delete
    fun delete(weather: WeatherEntity)

    @Query("SELECT * FROM weather")
    fun getAll(): Weather
}