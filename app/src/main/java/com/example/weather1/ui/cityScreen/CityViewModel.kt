package com.example.weather1.ui.cityScreen

import androidx.compose.runtime.saveable.Saver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.repositorys.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    // Создаёт изменяемый поток состояния с заданным начальным значением (CityState)
    private val _state = MutableStateFlow(CityState())

    // Аналог геттера для _state?
    val state = _state

    //
    val currentState = _state.value

    init {
        viewModelScope.launch {
            combineDataState().collectLatest { cityStateInfo ->
                cityStateInfo?.let {
                    _state.value = currentState.copy(city = it.city, region = it.region)
                }
            }
        }
    }

    private fun combineDataState(): Flow<CityStateInfo?> {
        return combine(
            repository.getCityFlow(),
            repository.getRegionFlow()
        ) { city: String?, region: Int? ->
            CityStateInfo(city ?: currentState.city, region ?: currentState.region)
        }
    }

    fun process(event: CityEvents) {
        when (event) {
            is CityEvents.UpdateCity -> updateCity(event.city)
            is CityEvents.UpdateRegion -> updateRegion(event.region)
            CityEvents.SaveScreen -> saveScreen()
        }
    }

    private fun updateCity(city: String) {
        _state.value = currentState.copy(city = city)
    }

    private fun updateRegion(region: String) {
        val currentRegion = region.filter { it.isDigit() }.take(6).toIntOrNull() ?: 0
        _state.value = currentState.copy(region = currentRegion)
    }

    private fun saveScreen() {
        viewModelScope.launch {
            repository.editRegion(currentState.region)
            repository.editCity(currentState.city)
        }
    }
}

data class CityState(val city: String = "", val region: Int = 0)

sealed class CityEvents {

    data class UpdateCity(val city: String) : CityEvents()
    data class UpdateRegion(val region: String) : CityEvents()
    object SaveScreen: CityEvents()

}

data class CityStateInfo(val city: String, val region: Int)