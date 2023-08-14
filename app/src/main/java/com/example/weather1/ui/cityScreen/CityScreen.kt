package com.example.weather1.ui.cityScreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.weather1.MainActivity
import com.example.weather1.R
import com.example.weather1.Weather
import com.example.weather1.WeatherState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityViewScreen(cityViewModel: CityViewModel = hiltViewModel()) {

    // Состояние загружающееся из cityViewModel
    val cityState: CityState by cityViewModel.readOnlyStateFlaw.collectAsState()
    //
    val lifecycle: LifecycleOwner = LocalLifecycleOwner.current

    val navController = rememberNavController()


    // Сробатывает при выходе из композиции
    DisposableEffect(Unit) {

        val observer = LifecycleEventObserver { lifecycleOwner, event ->
            when (event) {
                Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY -> {
                    cityViewModel.process(CityEvents.SaveScreen)
                }
            }
        }

        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }



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

        //BasicTextField("Enter location", { })
        TextField(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Color.Gray),
            value = cityState.cityName,
            // При изменении вызывет CityEvents.UpdateCity(city)
            onValueChange = { cityViewModel.process(CityEvents.UpdateCity(it)) },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        cityViewModel.process(CityEvents.AddCityToList(cityState.cityName))

                    },
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = null,
                    tint = Color.White
                )
            },
            placeholder = { Text(text = "Enter location") },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )


        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Log.e("test", "${cityState.cityList}")
            cityState.cityList.forEach {

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                cityViewModel.process(CityEvents.UpdateCity(it))
                            },
                            onLongClick = {
                                cityViewModel.process(CityEvents.RemoveCityFromList(it))
                            },
                        ),
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
                                Text(text = it)

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


    }
}

