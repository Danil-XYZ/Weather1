package com.example.weather1.ui.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.network.RespCurrentWeather
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
    private val locationProvider: LocationProvider
) : ViewModel() {

    // Создаётся изменяемый поток состояний
    private val mutableState = MutableStateFlow(MainState())

    val readOnlyState = mutableState.asStateFlow()

    private val currentState: MainState
        get() = readOnlyState.value

    // Выполняется сри создании CityViewModel
    init {
        // Создаётся новая нить
        viewModelScope.launch {
            mainRepository.getCityLocationFlow().collectLatest {
                val currentWeather = mainRepository.loadWeather(it?.lat.toString(), it?.lat.toString())
                mutableState.value = currentState.copy(screen = CheckMainScreen.MainView(currentWeather))
            }
        }
    }

    private fun updateLocation() {
        Log.e("MainViewModel", "location updated ${locationProvider.currentLocation()}")
        viewModelScope.launch {
            locationProvider.start()
            locationProvider.currentLocation()?.let {
                Log.e("MainViewModel", "location: $it")
                mainRepository.saveCityLocation(CityLocation(it.first, it.second))
            }
        }
    }

    fun process(event: MainEvents) {
        when(event){
            is MainEvents.UpdateLocation -> updateLocation()
        }
    }

}

data class CityLocation(val lat: Double =  37.6156, val lon: Double = 55.7522 )

data class MainState(
    val screen: CheckMainScreen = CheckMainScreen.Lodaing
)

sealed class MainEvents {
    object UpdateLocation: MainEvents()
}

sealed class CheckMainScreen {
    object Lodaing: CheckMainScreen()
    data class MainView(val currentWeather: RespCurrentWeather): CheckMainScreen()
}