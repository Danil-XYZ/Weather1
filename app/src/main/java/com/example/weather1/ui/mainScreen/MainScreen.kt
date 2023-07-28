package com.example.weather1.ui.mainScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.weather1.R
import com.example.weather1.Weather
import com.example.weather1.WeatherState
import com.example.weather1.ui.components.MenuItems
import java.time.LocalDate
import java.time.Period

@Composable
fun MainViewScreen(expanded: Boolean = true, onClickMenu: (MenuItems) -> Unit) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Log.e("MainScreen", "$screenHeight $screenWidth")



        // Main container
        Column(
            modifier = Modifier
                .height(screenHeight - 80.dp)
                .fillMaxWidth()
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                Column(modifier = Modifier.zIndex(1f)) {
                    if (expanded) {
                        BoxWithConstraints(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .height(0.dp)
                                .fillMaxWidth()
                        ) {
                            Surface(
                                elevation = 8.dp,
                                modifier = Modifier
                                    .wrapContentSize(Alignment.BottomCenter, unbounded = true)
                                    .width(maxWidth)
                                    .zIndex(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column() {
                                    listOf(MenuItems.Settings, MenuItems.Share).forEach() {
                                        Row(modifier = Modifier
                                            .height(44.dp)
                                            .clickable { onClickMenu(it) }) {

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Text(text = stringResource(id = it.title), color = Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                // Info conteiner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "20",
                    fontSize = 96.sp
                )

                Text(
                    text = "Clear 29/16",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /*TODO*/ },
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

@Composable
fun Forecast(weather: String = "Unknown") {


}

