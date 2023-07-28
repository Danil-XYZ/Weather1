package com.example.weather1.ui.cityScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CityViewScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Text(text = "It works!")
    }
}