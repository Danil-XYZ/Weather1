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
import com.example.weather1.network.RespError
import com.example.weather1.ui.cityScreen.CityState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(
    private val dataStore: AppDataStore,
    private val api: Api,
    private val weatherDao: WeatherDao,
    private val shortWeatherDao: ShortWeatherDao
) {




    suspend fun loadWeather(cityName: String): FullWeather? {
        if (Utils.noInternetConnection()) {
            dataStore.saveNotification("No internet connection")
            return null
        } else {
            val response = Utils.retryIO(
                1,
                error = { code, text -> RespCurrentWeather(error = RespError(text, code)) },
                block = {
                    api.currentWeatherByCity(cityName).body()
                }) ?: RespCurrentWeather()

            Log.e("test", "WeatherWithCity ${response}")

            response.error?.let {
                dataStore.saveNotification(it.errorText.toString())
                return null
            }

            val weather = response.toWeatherEntity()
            Log.e("test", "weather!!!!! $weather")

            val shortWeathers = response.weather.map {
                Log.e("test", "shortWeathers IT: ${it}")
                it.toShortWeatherEntity(weatherId = response.id!!.toLong())
            }

            weatherDao.insertWithShortWeather(weather, shortWeathers)

            val result = weatherDao.getById(weather.id)!!
            Log.e("test", "result $result")


            return result
        }
    }

    suspend fun getByName(city: String): FullWeather? {
        return weatherDao.getByName(city)
    }

    fun getFlowFull(): Flow<List<FullWeather>> {
        return weatherDao.getFlowFull().map { it ?: emptyList() }
    }

    fun getCityFlow(): Flow<CityState?> = dataStore.getCityFlow()

    suspend fun saveCity(city: CityState) = dataStore.saveCity(city)

    fun getWeatherId() = dataStore.getWeatherId()

    suspend fun saveWeatherId(weatherId: Long){
        dataStore.saveWeatherId(weatherId = weatherId)
    }

}