package com.example.weather1.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather1.db.entity.ShortWeatherEntity
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.Weather

@Dao
interface ShortWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: List<ShortWeatherEntity>)

    @Delete
    suspend fun delete(weather: ShortWeatherEntity)

    @Query("SELECT * FROM shortWeather")
    suspend fun getAll(): List<ShortWeatherEntity>
}