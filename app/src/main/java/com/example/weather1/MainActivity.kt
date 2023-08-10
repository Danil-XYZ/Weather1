package com.example.weather1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.weather1.ui.NavigationHost
import com.example.weather1.ui.RootState
import com.example.weather1.ui.RootViewModel
import com.example.weather1.ui.components.DrawerPanel
import com.example.weather1.ui.components.MenuItems
import com.example.weather1.ui.components.TopBarRow
import com.example.weather1.ui.mainScreen.MainViewScreen
import com.example.weather1.ui.theme.Weather1Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val vm: RootViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        setContent {

            val state: RootState by vm.state.collectAsState()
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(color = Color.Transparent)
            }

            LaunchedEffect(key1 = Unit) {
                launch {
                    navController.currentBackStackEntryFlow.collectLatest {
                        //vm.updateRout(it.destination.route)
                        it.destination.route?.let { it1 -> vm.updateRout(it1) }
                    }
                }
            }

            Weather1Theme {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                    if (state.currentRoute == "MainScreen") {
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
                        topBar = {

                            if (state.currentRoute == "MainScreen") {
                                TopBarRow(
                                    onClickFirst = {
                                        navController.navigate(route = "CityScreen")
                                    },
                                    onClicSecond = {
                                        scope.launch { scaffoldState.drawerState.open() }
                                    },
                                    title = "Moscow"
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


