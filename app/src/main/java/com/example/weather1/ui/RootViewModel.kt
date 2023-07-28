package com.example.weather1.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class RootViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(RootState())
    val state = _state
    val currentState = _state.value

    fun updateRout(route:String){
        _state.value = currentState.copy(currentRoute = route)
    }
}

data class RootState(val currentRoute: String = "MainScreen")
