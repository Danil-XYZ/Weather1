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
import com.example.weather1.ui.mainScreen.AvailableData.WeatherDefaults.defaults
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

    // Создаётся неизменяемый поток данных для чтения из вне
    val readOnlyStateFlaw = stateFlaw.asStateFlow()

    // Создаётся переменная типа MainState для получения данных из потока
    private val currentStateFlow: MainState
        get() = readOnlyStateFlaw.value

    // Выполняется сри создании MainViewModel
    init {
        // Запускается новая нить
        viewModelScope.launch {

            // Создаётся цикл в котором бесконечно получаются и обрабатываются данные из cityFlow
            cityRepository.getCityFlow().collectLatest {

                // Если город из MainState не совпадает с городом из памяти
                if (currentStateFlow.city != it?.city) {

                    // В stateFlaw копируются данные из памяти
                    stateFlaw.value = currentStateFlow.copy(
                        city = it?.city
                    )

                    Log.e("test", "${currentStateFlow.city}")

                    // Переменная, хранящая в себе Две таблицы и возможную ошибку
                    val currentWeather = mainRepository.loadWeather(availableData())

                    Log.e("test", "c: $currentWeather")

                    // Если в currentWeather зранится ошибка, то показать уведомление
                    currentWeather.error?.errorText?.let { massage->
                        mainRepository.saveNotification(massage)
                    }

                    // Сохраняется в поток новый статус с полученной погодой
                    stateFlaw.value = currentStateFlow.copy(
                        screenStatus = MainScreenStatus.IsLoadedWithWeather(currentWeather)
                    )
                }
            }
        }
    }

    private fun updateLocation() {
        Log.e("MainViewModel", "location updated ${locationProvider.currentLocation()}")
        viewModelScope.launch {
            locationProvider.start()
            locationProvider.currentLocation()?.let {
                Log.e("MainViewModel", "location: $it")
                mainRepository.saveCityLocation(CityCoordinates(it.first, it.second))
                stateFlaw.value = currentStateFlow.copy(
                    cityCoordinates = CityCoordinates(it.first, it.second)
                )
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
            currentStateFlow.city != null -> AvailableData.WeatherWithData(currentStateFlow.city!!)
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
    val defaults = WeatherEntity(name = "Moscow", main = Main(temp = 20.0))

    data class WeatherWithCoordinates(
        val coordinates: CityCoordinates, val defaultWeather: FullWeather =
            FullWeather(weatherEntity = defaults)
    ) : AvailableData()

    data class WeatherWithData(
        val city: String = "Moscow", val defaultWeather: FullWeather =
            FullWeather(weatherEntity = defaults)
    ) : AvailableData()

    object WeatherDefaults : AvailableData() {
        val defaultWeather =
            FullWeather(weatherEntity = defaults)
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