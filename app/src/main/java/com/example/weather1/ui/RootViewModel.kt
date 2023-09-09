package com.example.weather1.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather1.repositorys.CityRepository
import com.example.weather1.repositorys.MainRepository
import com.example.weather1.repositorys.RootRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RootViewModel @Inject constructor(
    private val mainRespository: MainRepository,
    private val cityRepository: CityRepository,
    private val rootRepository: RootRepository
) : ViewModel() {
    // Изменяемый поток состояний хранит RootState
    private val rootStateFlow = MutableStateFlow(RootState())

    val readOnlystate = rootStateFlow.asStateFlow()

    private val currentStateFlow
        get() = rootStateFlow.value

    init {
        viewModelScope.launch {
            // При загрузке приложения извлекает из памяти название города
            cityRepository.getCityFlow().collectLatest {
                rootStateFlow.value = currentStateFlow.copy(currentCity = it?.cityName ?: "Error")
            }
        }
    }

    fun updateRout(route: String) {
        rootStateFlow.value = currentStateFlow.copy(currentRoute = route)
        Log.e("MainActivity", "RootUpdated ${rootStateFlow.value}")

    }

    fun updateNotification(): Flow<String?> {
        return rootRepository.getNotification()
    }

    suspend fun removeNotification() {
        rootRepository.removeNotification()
    }

}

data class RootState(
    val currentRoute: String = "MainScreen",
    val currentCity: String = ""
)
