package com.example.weather1.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.repositorys.CityRepository
import com.example.weather1.repositorys.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RootViewModel @Inject constructor(
    private val mainRespository: MainRepository,
    private val cityRepository: CityRepository
) : ViewModel() {
    // Изменяемый поток состояний хранит RootState
    private val stateFlow = MutableStateFlow(RootState())

    val readOnlystate = stateFlow.asStateFlow()

    private val currentStateFlow
        get() = stateFlow.value

    init {
        viewModelScope.launch {
            // При загрузке приложения извлекает из памяти название города
            cityRepository.getCityFlow().collectLatest {
                stateFlow.value = currentStateFlow.copy(currentCity = it?.city ?: "Error")
            }
        }
    }

    fun updateRout(route: String) {
        stateFlow.value = currentStateFlow.copy(currentRoute = route)
        Log.e("MainActivity", "RootUpdated ${stateFlow.value}")

    }


}

data class RootState(val currentRoute: String = "MainScreen", val currentCity: String = "")
