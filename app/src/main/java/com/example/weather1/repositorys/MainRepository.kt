package com.example.weather1.repositorys


import android.util.Log
import com.example.weather1.network.Api
import com.example.weather1.network.RespCurrentWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val api: Api) {
    //
    suspend fun loadWeather(lat: String, lon: String): RespCurrentWeather{

        val response = api.currentWeather(lat, lon)
        Log.e("MainRepository", "${response.body()}")
        // Возвращает тело ответа
        return response.body() ?: RespCurrentWeather()
    }
}