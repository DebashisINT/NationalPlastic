package com.nationalplasticfsm.features.orderhistory.activitiesapi

/**
 * Created by Pratishruti on 30-11-2017.
 */
object LocationFetchRepositoryProvider {
    fun provideLocationFetchRepository(): LocationFetchRepository {
        return LocationFetchRepository(LocationFetchApi.create())
    }
}