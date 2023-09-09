package com.example.weather1.repositorys

import android.util.Log
import com.example.weather1.dataStore.AppDataStore
import com.example.weather1.db.dao.ShortWeatherDao
import com.example.weather1.db.dao.WeatherDao
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.helpers.toShortWeatherEntity
import com.example.weather1.helpers.toWeatherEntity
import com.example.weather1.network.Api
import com.example.weather1.network.RespCurrentWeather
import com.example.weather1.ui.cityScreen.CityState
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
        val cityState = dataStore.getCityFlow().firstOrNull() ?: CityState("Москва")

        val weather = response.toWeatherEntity()

        val shortWeathers = response.weather.map{
            it.toShortWeatherEntity(weatherId = response.id!!.toLong())
        }
        weatherDao.insertWithShortWeather(weather, shortWeathers)

        dataStore.saveCity(cityState.copy(cityName = response.name ?: "Москва"))
        return weatherDao.getByName(city = cityName) ?: FullWeather()
    }

    suspend fun getByName(city: String): FullWeather? {
        return weatherDao.getByName(city)
    }

    fun getCityFlow(): Flow<CityState?> = dataStore.getCityFlow()

    suspend fun saveCity(city: CityState) = dataStore.saveCity(city)

}