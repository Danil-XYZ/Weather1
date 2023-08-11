package com.example.weather1.ui.mainScreen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weather1.R
import com.example.weather1.Weather
import com.example.weather1.WeatherState
import java.time.LocalDate
import java.time.Period
import kotlin.math.roundToInt

@Composable
fun MainViewScreen(navController: NavController, mainViewModel: MainViewModel = hiltViewModel()) {


    val mainState: MainState by mainViewModel.readOnlyState.collectAsState()


    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Log.e("MainScreen", "$screenHeight $screenWidth")

    val permissionRequest =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions -> if(permissions.values.all { it }) mainViewModel.process(MainEvents.UpdateLocation)
        }

    LaunchedEffect(Unit) {
        permissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        when(val screen = mainState.screen){
            is CheckMainScreen.Lodaing -> {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is CheckMainScreen.MainView -> {
                // Main container
                Column(
                    modifier = Modifier
                        .height(screenHeight - 80.dp)
                        .fillMaxWidth()
                ) {

                    // Info conteiner
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = screen.currentWeather.weatherEntity.main?.temp?.roundToInt().toString(),
                            fontSize = 96.sp
                        )

                        Text(
                            text = "${screen.currentWeather.shortWeatherEntity.firstOrNull()?.description} 29/16",
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {  navController.navigate(route = "AirScreen") },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White.copy(alpha = 0.5f)),
                            shape = CircleShape,
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text(
                                text = "AQL 27",
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }


                    // Warning
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF3BA4E8).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(16.dp)

                    ) {
                        Icon(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(100)
                                )
                                .clickable { },
                            painter = painterResource(id = R.drawable.ic_baseline_warning_24),
                            contentDescription = null,
                            tint = Color.White,
                        )

                        Text(text = "Yellow Warning for High!!!!")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forecast
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF3BA4E8).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10)
                            )
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(
                                modifier = Modifier
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(100)
                                    )
                                    .clickable { },
                                painter = painterResource(id = R.drawable.ic_baseline_warning_24),
                                contentDescription = null,
                                tint = Color.White,
                            )

                            Text(text = "Yellow Warning for High!!!!")
                        }


                        val weatherList = listOf(
                            Weather(WeatherState.Clear, 20, 30),
                            Weather(WeatherState.Clear, 22, 33),
                            Weather(WeatherState.Clear, 21, 31)
                        )

                        var date = LocalDate.now()

                        weatherList.forEach {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Icon(
                                    modifier = Modifier
                                        .background(
                                            color = Color.Transparent,
                                            shape = RoundedCornerShape(100)
                                        )
                                        .clickable { },
                                    painter = painterResource(id = R.drawable.ic_baseline_warning_24),
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                                Text(text = date.dayOfWeek.toString())
                                Text(
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End,
                                    text = "${it.dayTemperature}/${it.nightTemperature}"
                                )
                                var period = Period.of(0, 0, 1)
                                date = date.plus(period)
                            }
                        }


                    }
                }
            }
        }


    }
}

@Composable
fun Forecast(weather: String = "Unknown") {


}

