package com.breezenationalplasticfsm.features.location.shopRevisitStatus

import com.breezenationalplasticfsm.features.location.shopdurationapi.ShopDurationApi
import com.breezenationalplasticfsm.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}