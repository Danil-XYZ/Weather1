package com.example.weather1.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weather1.App
import retrofit2.HttpException
import kotlinx.coroutines.delay

object Utils {
    fun noInternetConnection(context: Context = App.applicationContext()): Boolean {
        val networkInfo =
            (context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
                    as ConnectivityManager).activeNetworkInfo
        return networkInfo == null || !networkInfo.isConnected
    }

    suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE - 1,
        delay: Long = 3_000,
        error: ((Int?, String?) -> T)? = null,
        block: suspend () -> T
    ): T {
        var code: Int? = null
        var errorMessage: String? = null
        repeat(times) {
            try {
                return block()
            } catch (ignored: Throwable) {
                code = when {
                    ignored is HttpException -> ignored.code()
                    else -> null
                }
                errorMessage = ignored.toString()
                Log.e("test","Units: $ignored")
            }
            delay(delay)
        }
        return error?.invoke(code, errorMessage) ?: block()
    }
}