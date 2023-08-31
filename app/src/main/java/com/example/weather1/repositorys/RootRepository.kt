package com.example.weather1.repositorys

import com.example.weather1.dataStore.AppDataStore
import com.example.weather1.db.dao.ShortWeatherDao
import com.example.weather1.db.dao.WeatherDao
import com.example.weather1.network.Api
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootRepository @Inject constructor(
    private val dataStore: AppDataStore
) {
    fun getNotification(): Flow<String?>{
        return dataStore.getNotificationFlow()
    }

    suspend fun removeNotification() {
        dataStore.removeNotification()
    }
}