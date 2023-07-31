package com.example.weather1.ui.cityScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather1.R
import com.example.weather1.Weather
import com.example.weather1.WeatherState

@Composable
fun CityViewScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Manage cities",
            fontSize = 28.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Color.Gray),
            value = "Enter location",
            onValueChange = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFF3BA4E8),
            contentColor = Color.White,
            shape = RoundedCornerShape(20)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row {
                        Text(text = "Moscow")

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_location_on_24),
                            contentDescription = null
                        )
                    }

                    val weather = Weather(WeatherState.Clear, 20, 30)

                    Text(text = "AQL 23 ${weather.dayTemperature}/${weather.nightTemperature}")
                }


                Text(
                    text = "20",
                    fontSize = 32.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}