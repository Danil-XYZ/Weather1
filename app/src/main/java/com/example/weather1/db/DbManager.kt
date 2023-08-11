package com.example.weather1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.weather1.BuildConfig
import com.example.weather1.db.dao.ShortWeatherDao
import com.example.weather1.db.dao.WeatherDao
import com.example.weather1.db.entity.ShortWeatherEntity
import com.example.weather1.db.entity.WeatherEntity


@Database(
    entities = [WeatherEntity::class, ShortWeatherEntity::class],
    version = AppDb.DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = []
)

abstract class AppDb: RoomDatabase(){
    companion object{
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }

    class MyAutoMigration : AutoMigrationSpec

    abstract fun weatherDao(): WeatherDao

    abstract fun shortWeatherDao(): ShortWeatherDao
}

