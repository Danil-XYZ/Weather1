package com.example.weather1.ui.cityScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.repositorys.CityRepository
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
    private val stateFlow = MutableStateFlow(CityState())

    // Аналог геттера для _state?
    val readOnlyStateFlaw = stateFlow.asStateFlow()

    // Текущее состояние данных
    private val currentStateFlow: CityState
        get() = readOnlyStateFlaw.value

    // Выполняется сри создании CityViewModel
    init {
        // Создаётся новая нить
        viewModelScope.launch {
            // Получает данные из памяти через CityRepository
            repository.getCityFlow().collectLatest { collectedLatest ->
                collectedLatest?.let {
                    //Присваеваем потоку состояния полученные данные
                    stateFlow.value =
                        currentStateFlow.copy(cityName = it.city, cityList = it.cityList)
                }
            }
        }
    }

    // Присваевает изменяемому потоку значение city из памяти
    private fun updateCity(city: String) {
        Log.e("test1", "saveScreen ${city}")
        stateFlow.value = currentStateFlow.copy(cityName = city)
    }

    // Сохраняет в память текущие значения
    private fun saveScreen() {
        viewModelScope.launch {
            Log.e("test", "saveScreen ${currentStateFlow.cityList}")
            repository.saveCity(

                CurrentCityInfo(
                    city = currentStateFlow.cityName,
                    cityList = currentStateFlow.cityList
                )
            )
        }
    }

    // Присваевает изменяемому потоку значение cityList из памяти
    private fun addCityToList(city: String) {
        val list = currentStateFlow.cityList
        if (!list.contains(city)) {
            list.put(city, 20)
            stateFlow.value = currentStateFlow.copy(cityList = list)
            saveScreen()
        }
    }

    // Присваевает изменяемому потоку значение cityList из памяти
    private fun removeCityFromList(city: String) {
        val list = currentStateFlow.cityList
        list.remove(city)
        stateFlow.value = currentStateFlow.copy(cityList = list)
        saveScreen()
    }

    private fun getByName(city: String) {
        viewModelScope.launch {
            val temperature = repository.getByName(city)?.weatherEntity?.main?.temp?.toInt() ?: 20

            val list = currentStateFlow.cityList
            list[city] = temperature

            stateFlow.value = currentStateFlow.copy(
                 cityList = list
            )
        }
    }

    // Метод, обрабатывающий события
    fun process(cityEvent: CityEvents) {
        when (cityEvent) {
            is CityEvents.UpdateCity -> updateCity(cityEvent.cityName)
            is CityEvents.SaveScreen -> saveScreen()
            is CityEvents.AddCityToList -> addCityToList(cityEvent.cityName)
            is CityEvents.RemoveCityFromList -> removeCityFromList(cityEvent.cityName)
            is CityEvents.GetByName -> getByName(cityEvent.cityName)

        }
    }

}

data class CityState(
    val cityName: String = "",
    val cityList: MutableMap<String, Int> = mutableMapOf(Pair("Moscow", 20)),
)


sealed class CityEvents {
    data class UpdateCity(val cityName: String) : CityEvents()
    data class AddCityToList(val cityName: String) : CityEvents()
    data class RemoveCityFromList(val cityName: String) : CityEvents()
    object SaveScreen : CityEvents()
    data class GetByName(val cityName: String) : CityEvents()
}

data class CurrentCityInfo(val city: String, val cityList: MutableMap<String, Int> = mutableMapOf(Pair("Moscow", 20)))