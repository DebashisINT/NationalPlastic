package com.breezefieldnationalplastic.features.location.api

import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationApi
import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}