package com.example.weather1.repositorys

import android.util.Log
import com.example.weather1.dataStore.AppDataStore
import com.example.weather1.db.dao.ShortWeatherDao
import com.example.weather1.db.dao.WeatherDao
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.helpers.Utils
import com.example.weather1.helpers.toShortWeatherEntity
import com.example.weather1.helpers.toWeatherEntity
import com.example.weather1.network.Api
import com.example.weather1.network.RespCurrentWeather
import com.example.weather1.ui.cityScreen.CurrentCityInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(
    private val dataStore: AppDataStore,
    private val api: Api,
    private val weatherDao: WeatherDao,
    private val shortWeatherDao: ShortWeatherDao
) {




    suspend fun loadWeather(cityName: String): FullWeather {
        Log.e("test", "d")
        val response = api.currentWeatherByCity(city = cityName).body() ?: RespCurrentWeather()
        Log.e("test", "${response}")
        val cityStateInfo = dataStore.getCityFlow().firstOrNull() ?: CurrentCityInfo("Москва")

        val weather = response.toWeatherEntity()
        weatherDao.insertAll(weather)

        val shortWeathers = response.weather.map{
            it.toShortWeatherEntity(weatherId = response.id!!.toLong())
        }
        shortWeatherDao.insertAll(shortWeathers)

        dataStore.saveCity(cityStateInfo.copy(city = response.name ?: "Москва"))
        return weatherDao.getByName(city = cityName) ?: FullWeather()
    }

    suspend fun getByName(city: String): FullWeather? {
        return weatherDao.getByName(city)
    }

    fun getCityFlow(): Flow<CurrentCityInfo?> = dataStore.getCityFlow()

    suspend fun saveCity(city: CurrentCityInfo) = dataStore.saveCity(city)

}