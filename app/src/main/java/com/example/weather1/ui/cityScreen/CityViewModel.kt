package com.example.weather1.ui.cityScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.db.dao.WeatherDao
import com.example.weather1.db.entity.FullWeather
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
//        viewModelScope.launch {
//            // Получает данные из памяти через CityRepository
//            repository.getCityFlow().collectLatest { collectedLatest ->
//                collectedLatest?.let {
//                    //Присваеваем потоку состояния полученные данные
//                    stateFlow.value =
//                        currentStateFlow.copy(cityName = it.cityName, cityList = it.cityList)
//                }
//            }
//        }
//
//        viewModelScope.launch {
//            repository.getFlowFull().collectLatest {
//                val list: MutableList<String> = it.toMutableList()
//                Log.e("test", "names ${list}")
//                stateFlow.value = currentStateFlow.copy(cityLists = list)
//                Log.e("test", "names ${stateFlow.value.cityLists}")
//
//            }
//        }

        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            combineWeatherWithId().collectLatest { fullWeathers ->
                fullWeathers?.let {
                    stateFlow.value = currentStateFlow.copy(cityLists = fullWeathers.weathers.map {
                        it.weatherEntity.name ?: ""
                    }.toMutableList(),

                    cityName = fullWeathers.weathers.map {
                        it.weatherEntity.id to it.weatherEntity.name
                    }.firstOrNull{
                        it.first == fullWeathers.weatherId
                    }?.second ?: ""
                    )
                }
            }
        }
    }

    private suspend fun combineWeatherWithId () : Flow<WeatherWithId?> {
        return combine(
            repository.getFlowFull(),
            repository.getWeatherId()
        ) {
            fullWeathers: List<FullWeather>, weatherId: Long? ->
            WeatherWithId(weathers = fullWeathers, weatherId = weatherId)
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            Log.e("test2", "${currentStateFlow.cityName}")
            repository.loadWeather(currentStateFlow.cityName)?.let {
                stateFlow.value = currentStateFlow.copy(
                    weatherId = it.weatherEntity.id
                )
                updateWeatherId(it.weatherEntity.id)
            }
        }
    }

    // Присваевает изменяемому потоку значение city из памяти
    private fun updateCity(city: String) {
        Log.e("test1", "saveScreen ${city}")
        stateFlow.value = currentStateFlow.copy(cityName = city)
    }

    // Сохраняет в память текущие значения
    private fun updateCityInfo() {
        // В потоке записывает в CityRepository обект CurrentCityInfo
        viewModelScope.launch {
            repository.saveCity(
                CityState(
                    cityName = currentStateFlow.cityName,
                    cityList = currentStateFlow.cityList
                )
            )
        }
    }

    // Сохраняет в память текущие значения
    private suspend fun updateWeatherId(weatherId: Long) {
        // В потоке записывает в CityRepository обект CurrentCityInfo
        repository.saveWeatherId(weatherId)
    }

    // Присваевает изменяемому потоку значение cityList из памяти
    private fun addCityToList(city: String) {
        val list = currentStateFlow.cityList
        if (!list.contains(city)) {
            list.put(city, 20)
            stateFlow.value = currentStateFlow.copy(cityList = list)
            updateCityInfo()
        }
    }

    // Присваевает изменяемому потоку значение cityList из памяти
    private fun removeCityFromList(city: String) {
        val list = currentStateFlow.cityList
        list.remove(city)
        stateFlow.value = currentStateFlow.copy(cityList = list)
        updateCityInfo()
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
            is CityEvents.UpdateCityInfo -> updateCityInfo()
            is CityEvents.AddCityToList -> addCityToList(cityEvent.cityName)
            is CityEvents.RemoveCityFromList -> removeCityFromList(cityEvent.cityName)
            is CityEvents.GetByName -> getByName(cityEvent.cityName)
            is CityEvents.LoadWeather -> loadWeather()

        }
    }

}

data class CityState(
    val weatherId: Long? = null,
    val cityName: String = "",
    val cityList: MutableMap<String, Int> = mutableMapOf(Pair("Moscow", 20)),
    val cityLists: MutableList<String> = mutableListOf("Moscow")
)

data class WeatherWithId (val weathers: List<FullWeather>, val weatherId: Long?)



sealed class CityEvents {
    object LoadWeather : CityEvents()
    data class UpdateCity(val cityName: String) : CityEvents()
    data class AddCityToList(val cityName: String) : CityEvents()
    data class RemoveCityFromList(val cityName: String) : CityEvents()
    object UpdateCityInfo : CityEvents()
    data class GetByName(val cityName: String) : CityEvents()
}
