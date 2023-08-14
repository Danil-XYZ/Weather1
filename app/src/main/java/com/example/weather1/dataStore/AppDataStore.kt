package com.example.weather1.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.weather1.ui.cityScreen.CurrentCityInfo
import com.example.weather1.ui.mainScreen.CityCoordinates
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
class AppDataStore @Inject constructor(private val dataStore: DataStore<Preferences>, val moshi: Moshi) {

    private object PreferencesKeys {
        val CITY_INFO = stringPreferencesKey("cityInfo")
        val CITY_LOCATION = stringPreferencesKey("cityLocation")
    }



    fun getCityFlow(): Flow<CurrentCityInfo?> {
        return dataStore.data.map{pref -> moshi.parseObj(pref[PreferencesKeys.CITY_INFO])}
    }

    suspend fun saveCity(city: CurrentCityInfo?) {
        dataStore.edit{pref -> pref[PreferencesKeys.CITY_INFO] = moshi.toJson(city)}
    }


    fun getCityLocationFlow(): Flow<CityCoordinates?> {
        return dataStore.data.map{pref -> moshi.parseObj(pref[PreferencesKeys.CITY_LOCATION])}
    }

    suspend fun saveCityLocation(location: CityCoordinates?) {
        dataStore.edit{pref -> pref[PreferencesKeys.CITY_LOCATION] = moshi.toJson(location)}
    }

}

inline fun <reified T> Moshi.parseList(jsonString: String?): List<T> {
    return jsonString?.let {
        adapter<List<T>>(
            Types.newParameterizedType(
                List::class.java,
                T::class.java
            )
        ).fromJson(it)
    } ?: emptyList()
}

inline fun <reified T> Moshi.parseObj(jsonString: String?): T? {
    return jsonString?.let { adapter(T::class.java).fromJson(jsonString) }
}

inline fun <reified T> Moshi.toJson(list: List<T>): String {
    return adapter<List<T>>(
        Types.newParameterizedType(
            List::class.java,
            T::class.java
        )
    ).toJson(list)
}

inline fun <reified T> Moshi.toJson(obj: T): String {
    return adapter(T::class.java).toJson(obj)
}