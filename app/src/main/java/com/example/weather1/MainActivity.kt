package com.example.weather1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.weather1.ui.components.DrawerPanel
import com.example.weather1.ui.components.MenuItems
import com.example.weather1.ui.components.TopBarRow
import com.example.weather1.ui.mainScreen.MainViewScreen
import com.example.weather1.ui.theme.Weather1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            Weather1Theme {

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxSize()
                    )

                    Scaffold(
                        contentColor = MaterialTheme.colors.onBackground,
                        scaffoldState = scaffoldState,
                        backgroundColor = Color.Transparent,
                        topBar = {
                            TopBarRow(
                                onClickFirst = { /*TODO*/ },
                                onClicSecond = {
                                    scope.launch { scaffoldState.drawerState.open() }
                                },
                                title = "Moscow"
                            )
                        },
                        drawerContent = {
                            DrawerPanel(
                                listOf(MenuItems.Settings, MenuItems.Share),
                                MenuItems.Settings
                            ) { scope.launch { scaffoldState.drawerState.close() } }
                        }
                    ) {
                        MainViewScreen()
                    }
                }
            }
        }
    }
}


