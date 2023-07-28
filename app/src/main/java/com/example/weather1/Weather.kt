package com.example.weather1

import androidx.annotation.StringRes

data class Weather(val weatherState: WeatherState, val dayTemperature: Int, val nightTemperature: Int)



enum class WeatherState(@StringRes val title: Int){
    Clear(title = R.string.clear),
    Cloudy(title = R.string.cloudy),
    Rainy(title = R.string.rainy),
    Foggy(title = R.string.foggy)
}