package com.example.weather1.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val locationManager =
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    private var currentLocation: Location? = null

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun start() {
        registerLocationUpdate()
    }

    fun currentLocation(): Pair<Double, Double>? =
        currentLocation?.let { it.latitude to it.longitude }

    fun stop() {
        locationManager.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationUpdate() {
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.e("MainViewModel", "gpsEnabled: $gpsEnabled")


        val provider =
            if (gpsEnabled) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            0f,
            locationListener
        )
        currentLocation = locationManager.getLastKnownLocation(provider)
    }
}