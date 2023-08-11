package com.example.weather1.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.weather1.db.AppDb
import com.example.weather1.db.dao.ShortWeatherDao
import com.example.weather1.db.dao.WeatherDao
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val WEATHER = "weather"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }),
            migrations = listOf(
                SharedPreferencesMigration(
                    appContext, "ru.weather1.weather_preferences"
                )
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(WEATHER) })
    }


    @Singleton
    @Provides
    fun provideMoshi(): Moshi = JsonParser.moshi

    @Provides
    @Singleton
    fun appDb(@ApplicationContext context: Context): AppDb = Room.databaseBuilder(
        context,
        AppDb::class.java,
        AppDb.DATABASE_NAME
    ).fallbackToDestructiveMigrationOnDowngrade().build()

    @Provides
    @Singleton
    fun weatherDao(db: AppDb): WeatherDao = db.weatherDao()

    @Provides
    @Singleton
    fun shortWeatherDao(db: AppDb): ShortWeatherDao = db.shortWeatherDao()


}

object JsonParser{
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
}