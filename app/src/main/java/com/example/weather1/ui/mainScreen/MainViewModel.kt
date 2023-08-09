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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor(
    mainRepository: MainRepository,
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
            val currentWeather = mainRepository.loadWeather("53.03000", "49.3461000")
            mutableState.value = currentState.copy(screen = CheckMainScreen.MainView(currentWeather))
        }
    }

    private fun updateLocation() {
        Log.e("MainViewModel", "location updated ${locationProvider.currentLocation()}")

        locationProvider.start()
        locationProvider.currentLocation()?.let {
            Log.e("MainViewModel", "location: $it")
            mutableState.value = currentState.copy(lat = it.first, lon = it.second)
        }
    }

    fun process(event: MainEvents) {
        when(event){
            is MainEvents.UpdateLocation -> updateLocation()
        }
    }

}


data class MainState(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val screen: CheckMainScreen = CheckMainScreen.Lodaing
)

sealed class MainEvents {
    object UpdateLocation: MainEvents()
}

sealed class CheckMainScreen {
    object Lodaing: CheckMainScreen()
    data class MainView(val currentWeather: RespCurrentWeather): CheckMainScreen()
}