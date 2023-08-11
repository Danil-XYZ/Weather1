package com.example.weather1.helpers

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.example.weather1.App

object Utils {
    fun noInternetConnection(context: Context = App.applicationContext()): Boolean {
        val networkInfo =
            (context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
                    as ConnectivityManager).activeNetworkInfo
        return networkInfo == null || !networkInfo.isConnected
    }
}