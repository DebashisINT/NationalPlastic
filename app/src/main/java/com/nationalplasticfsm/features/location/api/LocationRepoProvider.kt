package com.nationalplasticfsm.features.location.api

import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationApi
import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}