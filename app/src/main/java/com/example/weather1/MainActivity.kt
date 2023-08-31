package com.example.weather1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.weather1.ui.NavigationHost
import com.example.weather1.ui.RootViewModel
import com.example.weather1.ui.components.DrawerPanel
import com.example.weather1.ui.components.MenuItems
import com.example.weather1.ui.components.TopBarRow
import com.example.weather1.ui.theme.Weather1Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val rootViewModel: RootViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Элементы декора окна могут занимать пространство StatusBar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()

            SideEffect {
                // Меняет цвет state bar на прозрачный
                systemUiController.setStatusBarColor(color = Color.Transparent)
            }

            // объект класса RootState
            val rootState by rootViewModel.readOnlystate.collectAsState()
            //основной поток
            val scope = rememberCoroutineScope()

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            // Получает последние данные из потока
            LaunchedEffect(key1 = Unit) {
                launch {
                    // По неизвестной причине тело этого метода выполняется каждый раз при смене экрана
                    navController.currentBackStackEntryFlow.collectLatest {
                        // Обновляет rout в RootViewModel
                        it.destination.route?.let {
                            rootViewModel.updateRout(it)
                        }
                    }
                }

                launch {

                    rootViewModel.updateNotification().collectLatest {
                        Log.e("test", "updateNotification ${it}")
                        it?.let {
                            scaffoldState.snackbarHostState.showSnackbar(message = it)
                            rootViewModel.removeNotification()
                        }
                    }

                }
            }


            Weather1Theme {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                    if (rootState.currentRoute == "MainScreen") {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.fillMaxSize()
                        )
                    }



                    Scaffold(
                        contentColor = MaterialTheme.colors.onBackground,
                        scaffoldState = scaffoldState,
                        backgroundColor = Color.Transparent,
                        snackbarHost = { host ->
                            Log.e("test", "SnackbarHost ${host}")
                            SnackbarHost(
                                hostState = host,
                                snackbar = { Snackbar (snackbarData = it) },
                                modifier = Modifier.offset(y = -48.dp)
                            )
                        },
                        topBar = {

                            if (rootState.currentRoute == "MainScreen") {
                                TopBarRow(
                                    onClickFirst = {
                                        navController.navigate(route = "CityScreen")
                                    },
                                    onClicSecond = {
                                        scope.launch { scaffoldState.drawerState.open() }
                                    },
                                    title = rootState.currentCity
                                )
                            } else {
                                TopBarRow(
                                    onClickFirst = {
                                        navController.popBackStack()
                                    },
                                    iconPainterSecond = null,
                                    title = null,
                                    iconPainterFirst = painterResource(id = R.drawable.baseline_keyboard_backspace_24)
                                )
                            }

                        },
                        drawerContent = {
                            DrawerPanel(
                                listOf(MenuItems.Settings, MenuItems.Share),
                                MenuItems.Settings
                            ) {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        }
                    ) {
                        NavigationHost(navController = navController)
                    }
                }
            }
        }
    }
}


