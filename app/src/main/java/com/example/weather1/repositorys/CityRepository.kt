package com.example.weather1.repositorys

import com.example.weather1.dataStore.AppDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

//interface ICityRepository {
//    fun getCityFlow(): Flow<String?>
//    suspend fun editCity(city: String)
//    suspend fun removeCity()
//}
//
//@Singleton
//class CityRepository @Inject constructor(private val dataStore: AppDataStore) : ICityRepository {
//    override fun getCityFlow(): Flow<String?> = dataStore.getCityFlow()
//
//    override suspend fun editCity(city: String) = dataStore.editCity(city)
//
//    override suspend fun removeCity() = dataStore.removeCity()
//}

@Singleton
class CityRepository @Inject constructor(private val dataStore: AppDataStore) {
    fun getCityFlow(): Flow<String?> = dataStore.getCityFlow()

    suspend fun editCity(city: String) = dataStore.editCity(city)

    suspend fun removeCity() = dataStore.removeCity()

    fun getRegionFlow(): Flow<Int?> = dataStore.getRegionFlow()

    suspend fun editRegion(region: Int) = dataStore.editRegion(region)

    suspend fun removeRegion() = dataStore.removeRegion()
}