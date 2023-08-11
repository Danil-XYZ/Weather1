package com.example.weather1.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.Weather

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: WeatherEntity)

    @Delete
    suspend fun delete(weather: WeatherEntity)

    @Query("SELECT * FROM weather")
    suspend fun getAll(): WeatherEntity

    @Transaction
    @Query("SELECT * FROM weather")
    suspend fun getFull(): List<FullWeather>
}