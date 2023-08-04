package com.example.weather1.dataStore

import android.graphics.Region
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//interface IAppDataStore {
//    fun getCityFlow(): Flow<String?>
//    suspend fun editCity(city: String)
//    suspend fun removeCity()
//}
//
//@Singleton
//class AppDataStore @Inject constructor(private val dataStore: DataStore<Preferences>): IAppDataStore {
//    private object PreferencesKeys {
//        val CITY = stringPreferencesKey("city")
//    }
//
//    override fun getCityFlow(): Flow<String?> {
//        return dataStore.data.map{pref -> pref[PreferencesKeys.CITY]}
//    }
//
//    override suspend fun editCity(city: String) {
//        dataStore.edit{pref -> pref[PreferencesKeys.CITY] = city}
//    }
//
//    override suspend fun removeCity() {
//        dataStore.edit {pref -> pref.remove(PreferencesKeys.CITY)}
//    }
//}

@Singleton
class AppDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val CITY = stringPreferencesKey("city")
        val REGION = intPreferencesKey("region")
    }



    fun getCityFlow(): Flow<String?> {
        return dataStore.data.map{pref -> pref[PreferencesKeys.CITY]}
    }

    suspend fun editCity(city: String) {
        dataStore.edit{pref -> pref[PreferencesKeys.CITY] = city}
    }

    suspend fun removeCity() {
        dataStore.edit {pref -> pref.remove(PreferencesKeys.CITY)}
    }



    fun getRegionFlow(): Flow<Int?> {
        return dataStore.data.map{pref -> pref[PreferencesKeys.REGION]}
    }

    suspend fun editRegion(region: Int) {
        dataStore.edit{pref -> pref[PreferencesKeys.REGION] = region}
    }

    suspend fun removeRegion() {
        dataStore.edit {pref -> pref.remove(PreferencesKeys.REGION)}
    }
}