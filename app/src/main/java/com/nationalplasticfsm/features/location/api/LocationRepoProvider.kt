package com.nationalplasticfsm.features.location.api

import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationApi
import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationRepository

/**
 * Created by Saikat on 17-Aug-20.
 */
object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}