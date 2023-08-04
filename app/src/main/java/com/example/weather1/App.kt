package com.example.weather1

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        private lateinit var instance: App
        fun applicationContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}