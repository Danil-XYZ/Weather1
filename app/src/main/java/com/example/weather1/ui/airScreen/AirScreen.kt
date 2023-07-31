package com.example.weather1.ui.airScreen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather1.R
import com.example.weather1.Weather
import com.example.weather1.WeatherState


@Composable
fun AirViewScreen() {

    val color = Color(0xFF09945B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {

        Text(text = "Air Quality Index", fontSize = 28.sp)
        Text(text = "Moscow Published at 8:00 PM")

        Spacer(modifier = Modifier.height(32.dp))

        Row (verticalAlignment = Alignment.Bottom){
            Text(text = "27", color = color, fontSize = 56.sp)
            Text(text = "Good", color = color, modifier = Modifier.padding(12.dp), fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))


        Text(text = "Air quality is good. A perfect day for a walk!")

        Spacer(modifier = Modifier.height(8.dp))

        Row (modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween, ){
            Text(text = "23.4", color = color, fontSize = 20.sp)
            Text(text = "14.4", color = color, fontSize = 20.sp)
            Text(text = "0.6", color = color, fontSize = 20.sp)
            Text(text = "3.3", color = color, fontSize = 20.sp)
            Text(text = "17.4", color = color, fontSize = 20.sp)
            Text(text = "1.4", color = color, fontSize = 20.sp)
        }
    }
}