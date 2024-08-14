package com.breezenationalplasticfsm.features.location.api

import com.breezenationalplasticfsm.features.location.shopdurationapi.ShopDurationApi
import com.breezenationalplasticfsm.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}