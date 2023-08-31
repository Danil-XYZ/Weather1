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
import com.example.weather1.ui.cityScreen.CurrentCityInfo
import com.example.weather1.ui.mainScreen.AvailableData
import com.example.weather1.ui.mainScreen.CityCoordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val api: Api,
    private val dataStore: AppDataStore,
    private val weatherDao: WeatherDao,
    private val shortWeatherDao: ShortWeatherDao
) {

//    suspend fun loadWeather(lat: String, lon: String): FullWeather {
//
//        Log.e("test", "Internet connection: ${Utils.noInternetConnection()}")
//        if (!Utils.noInternetConnection()) {
//            if (lat == "null" || lon == "null") {
//
//                Log.e("c", "weatherDao ${weatherDao.getFull().firstOrNull()}")
//                return weatherDao.getFull().firstOrNull() ?: FullWeather()
//            }
////            val response = api.currentWeather(lat, lon).body() ?: RespCurrentWeather()
//            val response = Utils.retryIO(
//                1,
//                error = { _, _ -> RespCurrentWeather() },
//                block = { api.currentWeather(lat, lon).body() }) ?: RespCurrentWeather()
//            Log.e("MainRepository", "${response}")
//            val cityStateInfo = dataStore.getCityFlow().firstOrNull() ?: CurrentCityInfo("Москва")
//
//            val weather = response.toWeatherEntity()
//            weatherDao.insertAll(weather)
//            val shortWeathers = response.weather.map {
//                it.toShortWeatherEntity(weatherId = response.id!!.toLong())
//            }
//            shortWeatherDao.insertAll(shortWeathers)
//
//            dataStore.saveCity(cityStateInfo.copy(city = response.name ?: "Москва"))
//
//            return weatherDao.getFull().firstOrNull() ?: FullWeather()
//        } else {
//            return weatherDao.getFull().firstOrNull() ?: FullWeather()
//
//        }
//
//    }

    suspend fun loadWeather(availableData: AvailableData): FullWeather {
        Log.e("test", "$availableData")
        when (availableData) {
            is AvailableData.WeatherDefaults -> return availableData.defaultWeather
            is AvailableData.WeatherWithCoordinates -> {
                if (Utils.noInternetConnection()) return availableData.defaultWeather
                else {
                    val response = Utils.retryIO(
                        1,
                        error = { code, text -> RespCurrentWeather(error = RespError(text, code)) },
                        block = {
                            api.currentWeather(
                                availableData.coordinates.lat.toString(),
                                availableData.coordinates.lon.toString()
                            ).body()
                        }) ?: RespCurrentWeather()

                    Log.e("test", "WeatherWithCoordinates ${response.error}")
                    response.error?.errorText?.let {
                        dataStore.saveNotification(it)
                    }

//                    val cityStateInfo = dataStore.getCityFlow().firstOrNull() ?: CurrentCityInfo("Москва")

                    val weather = response.toWeatherEntity()
                    weatherDao.insertAll(weather)
                    val shortWeathers = response.weather.map {
                        it.toShortWeatherEntity(weatherId = response.id!!.toLong())
                    }
                    shortWeatherDao.insertAll(shortWeathers)

//                    dataStore.saveCity(cityStateInfo.copy(city = response.name ?: "Москва"))

                    return weatherDao.getFull().firstOrNull() ?: FullWeather()
                }
            }
            is AvailableData.WeatherWithCity -> {
                if (Utils.noInternetConnection()) {
                    return weatherDao.getByName(city = availableData.city) ?: availableData.defaultWeather
                }
                else {
                    val response = Utils.retryIO(
                        1,
                        error = { code, text -> RespCurrentWeather(error = RespError(text, code)) },
                        block = {
                            api.currentWeatherByCity(availableData.city).body()
                        }) ?: RespCurrentWeather()

                    Log.e("test", "WeatherWithCity ${response.error}")
                    response.error?.errorText?.let {
                        dataStore.saveNotification(it)
                    }

                    val weather = response.toWeatherEntity()
                    weatherDao.insertAll(weather)
                    val shortWeathers = response.weather.map {
                        it.toShortWeatherEntity(weatherId = response.id!!.toLong())
                    }
                    shortWeatherDao.insertAll(shortWeathers)

                    return weatherDao.getFull().firstOrNull() ?: FullWeather()
                }
            }
        }
    }

    fun getCityLocationFlow(): Flow<CityCoordinates?> {
        return dataStore.getCityLocationFlow()
    }

    suspend fun saveCityLocation(location: CityCoordinates?) {
        dataStore.saveCityLocation(location)
    }
}