package com.example.weather1.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weather1.ui.cityScreen.CityViewScreen
import com.example.weather1.ui.mainScreen.MainViewScreen

@Composable
fun NavigationHost(navController: NavController){
    NavHost(navController = navController as NavHostController, startDestination = "MainScreen"){
        composable("MainScreen"){
            MainViewScreen()
        }

        composable("CityScreen"){
            CityViewScreen()
        }
    }
}