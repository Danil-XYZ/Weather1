package com.example.weather1.ui.cityScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.repositorys.CityRepository
import com.example.weather1.ui.base.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    // Создаёт изменяемый поток состояния с заданным начальным значением (CityState("", 0))
    private val mutableState = MutableStateFlow(CityState())

    // Аналог геттера для _state?
    val readOnlyState = mutableState.asStateFlow()


    // Текущее состояние данных
    private val currentState: CityState
        get() = readOnlyState.value

    // Выполняется сри создании CityViewModel
    init {
        // Создаётся новая нить
        viewModelScope.launch {
            // Получает данные из памяти через CityRepository
            repository.getCityFlow().collectLatest { collectedLatest ->
                collectedLatest?.let {
                    //Присваеваем мотоку состояния полученные данные
                    mutableState.value = currentState.copy(city = it.city, region = it.region)
                }
            }
        }
    }

    // Метод, обрабатывающий события
    fun process(cityEvent: CityEvents) {
        when (cityEvent) {
            is CityEvents.UpdateCity -> updateCity(cityEvent.city)
            is CityEvents.UpdateRegion -> updateRegion(cityEvent.region)
            is CityEvents.SaveScreen -> saveScreen()
        }
    }

    // Присваевает изменяемому потоку значение city из памяти
    private fun updateCity(city: String) {
        mutableState.value = currentState.copy(city = city)
    }

    // Присваевает изменяемому потоку значение region из памяти
    private fun updateRegion(region: String) {
        // Принимает только первые 6 чисел
        val currentRegion = region.filter { it.isDigit() }.take(6).toIntOrNull() ?: 0
        mutableState.value = currentState.copy(region = currentRegion)
    }

    // Сохраняет в память текущие значения
    private fun saveScreen() {
        viewModelScope.launch {
            val cityStateInfo =
                CityStateInfo(city = currentState.city, region = currentState.region)
            repository.saveCity(cityStateInfo)
        }
    }
}

data class CityState(val city: String = "", val region: Int = 0)

sealed class CityEvents {
    data class UpdateCity(val city: String) : CityEvents()
    data class UpdateRegion(val region: String) : CityEvents()
    object SaveScreen : CityEvents()
}

data class CityStateInfo(val city: String, val region: Int)