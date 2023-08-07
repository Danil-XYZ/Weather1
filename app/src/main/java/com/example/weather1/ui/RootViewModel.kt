package com.example.weather1.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class RootViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(RootState())
    val state = _state.asStateFlow()
    private val currentState
        get() = _state.value

    fun updateRout(route:String){
        _state.value = currentState.copy(currentRoute = route)
    }

    fun updateIsBack(isBack: Boolean){
        _state.value = currentState.copy(isBack = isBack)
    }

}

data class RootState(val currentRoute: String = "MainScreen", val isBack: Boolean = false)
