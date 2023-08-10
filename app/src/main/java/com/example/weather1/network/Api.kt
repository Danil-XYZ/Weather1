package com.example.weather1.network

import com.example.weather1.di.Constants
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Response


interface Api {
    @POST("/data/2.5/weather")
    suspend fun currentWeather(
        @Query("lat")lat:String,
        @Query("lon")lon:String,
        @Query("appid")appid:String = Constants.API_KEY,
        @Query("lang")lang:String = "ru",
        @Query("units")units:String = "metric"
    ):Response<RespCurrentWeather>

    @POST("/data/2.5/weather")
    suspend fun currentWeatherByCity(
        @Query("q")city:String,
        @Query("appid")appid:String = Constants.API_KEY,
        @Query("lang")lang:String = "ru",
        @Query("units")units:String = "metric"
    ):Response<RespCurrentWeather>
}
