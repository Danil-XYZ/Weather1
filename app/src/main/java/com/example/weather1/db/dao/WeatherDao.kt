package com.example.weather1.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.db.entity.ShortWeatherEntity
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.network.Weather


// Data Access Object WeatherDao
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: WeatherEntity)

    @Update
    suspend fun update(weather: WeatherEntity)

    @Delete
    suspend fun delete(weather: WeatherEntity)

    @Query("SELECT * FROM weather")
    suspend fun getAll(): List<WeatherEntity>

    @Transaction
    @Query("SELECT * FROM weather")
    suspend fun getFull(): List<FullWeather>

    @Query("SELECT * FROM weather WHERE name =:city")
    suspend fun getByName(city: String): FullWeather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortWeathers(shortWeathersEntity: List<ShortWeatherEntity>)

    @Transaction
    suspend fun insertWithShortWeather(weather: WeatherEntity, shortWeathersEntity: List<ShortWeatherEntity>) {
        insertAll(weather)
        insertShortWeathers(shortWeathersEntity)
    }
}