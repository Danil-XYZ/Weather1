package com.example.weather1.ui.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.db.entity.WeatherEntity
import com.example.weather1.helpers.Utils
import com.example.weather1.network.Main
import com.example.weather1.repositorys.CityRepository
import com.example.weather1.repositorys.MainRepository
import com.example.weather1.ui.base.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val cityRepository: CityRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    // Создаётся изменяемый поток состояний
    private val stateFlaw = MutableStateFlow(MainState())

    val readOnlyStateFlaw = stateFlaw.asStateFlow()

    private val currentStateFlow: MainState
        get() = readOnlyStateFlaw.value

    // Выполняется сри создании MainViewModel

    suspend fun initScreen() {
        // Принимается массив координат
        Log.e("test", "1")

        cityRepository.getCityFlow().collectLatest {

            if (
                currentStateFlow.screenStatus is MainScreenStatus.IsLoadedWithWeather
                && currentStateFlow.city != null
                && currentStateFlow.city == it?.city
            ) {
                return@collectLatest
            }

            stateFlaw.value = currentStateFlow.copy(city = it?.city)

            Log.e("test", "${currentStateFlow.city}")

            val currentWeather = mainRepository.loadWeather(availableData())
            Log.e("test", "c: $currentWeather")

            currentWeather.error?.errorText?.let { massage ->
                mainRepository.saveNotification(massage)
            }

            stateFlaw.value = currentStateFlow.copy(
                screenStatus = MainScreenStatus.IsLoadedWithWeather(currentWeather)
            )

        }
    }

    private fun updateLocation() {
        viewModelScope.launch {
            locationProvider.start()
            locationProvider.currentLocation()?.let {
                Log.e("MainViewModel", "location: $it")
                stateFlaw.value = currentStateFlow.copy(
                    cityCoordinates = CityCoordinates(it.first, it.second)
                )
                mainRepository.saveCityLocation(CityCoordinates(it.first, it.second))
            }
        }
    }

    fun process(event: MainEvents) {
        when (event) {
            is MainEvents.UpdateLocation -> updateLocation()
            is MainEvents.UpdatePermission -> updatePermission(event.isPermission)
        }
    }

    fun availableData(): AvailableData {
        return when {
            Utils.noInternetConnection() && !currentStateFlow.isPermission -> AvailableData.WeatherDefaults
            currentStateFlow.city != null -> AvailableData.WeatherWithCity(currentStateFlow.city!!)
            currentStateFlow.cityCoordinates.lat != null && currentStateFlow.cityCoordinates.lon != null ->
                AvailableData.WeatherWithCoordinates(currentStateFlow.cityCoordinates)

            else -> AvailableData.WeatherDefaults
        }
    }

    private fun updatePermission(isPermission: Boolean) {
        if (isPermission) updateLocation()
        stateFlaw.value = currentStateFlow.copy(
            isPermission = isPermission
        )
    }
}

data class CityCoordinates(val lat: Double? = null, val lon: Double? = null)

data class MainState(
    val screenStatus: MainScreenStatus = MainScreenStatus.IsLodaing,
    val city: String? = null,
    val isPermission: Boolean = false,
    val cityCoordinates: CityCoordinates = CityCoordinates()
)

sealed class AvailableData {
    data class WeatherWithCoordinates(
        val coordinates: CityCoordinates, val defaultWeather: FullWeather =
            FullWeather(weatherEntity = WeatherEntity(name = "Moscow", main = Main(temp = 20.0)))
    ) : AvailableData()

    data class WeatherWithCity(
        val city: String = "Moscow", val defaultWeather: FullWeather =
            FullWeather(weatherEntity = WeatherEntity(name = "Moscow", main = Main(temp = 20.0)))
    ) : AvailableData()

    object WeatherDefaults : AvailableData() {
        val defaultWeather =
            FullWeather(weatherEntity = WeatherEntity(name = "Moscow", main = Main(temp = 20.0)))
    }
}

sealed class MainEvents {
    object UpdateLocation : MainEvents()
    data class UpdatePermission(val isPermission: Boolean) : MainEvents()
}

sealed class MainScreenStatus {
    object IsLodaing : MainScreenStatus()
    data class IsLoadedWithWeather(val currentWeather: FullWeather) : MainScreenStatus()
}