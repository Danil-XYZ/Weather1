package com.example.weather1.ui.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.db.entity.FullWeather
import com.example.weather1.repositorys.CityRepository
import com.example.weather1.repositorys.MainRepository
import com.example.weather1.ui.base.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    init {
        // Создаётся новая нить
        viewModelScope.launch {
            // Принимается массив координат
            Log.e("test", "1")

            var currentCity = "Samara"
//            cityRepository.getCityFlow().collectLatest {
//                it?.let {
//                    currentCity = it.city
//                }
//            }

            Log.e("test", "$currentCity")

            if (currentCity == "null") {
                Log.e("test", "a")
                // Из памяти загружаются две переменные
                mainRepository.getCityLocationFlow().collectLatest {
                    val currentWeather =
                        mainRepository.loadWeather(it?.lat.toString(), it?.lon.toString())
                    stateFlaw.value = currentStateFlow.copy(
                        screenStatus = MainScreenStatus.IsLoadedWithWeather(currentWeather)
                    )
                }
            } else {
                Log.e("test", "b")
                val currentWeather =
                    cityRepository.loadWeather(currentCity)
                stateFlaw.value = currentStateFlow.copy(
                    screenStatus = MainScreenStatus.IsLoadedWithWeather(currentWeather)
                )
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
            }
        }
    }

    fun process(event: MainEvents) {
        when (event) {
            is MainEvents.UpdateLocation -> updateLocation()
        }
    }

}

data class CityCoordinates(val lat: Double? = null, val lon: Double? = null)

data class MainState(
    val screenStatus: MainScreenStatus = MainScreenStatus.IsLodaing
)

sealed class MainEvents {
    object UpdateLocation : MainEvents()
}

sealed class MainScreenStatus {
    object IsLodaing : MainScreenStatus()
    data class IsLoadedWithWeather(val currentWeather: FullWeather) : MainScreenStatus()
}