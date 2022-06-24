package com.nationalplasticfsm.features.location.shopRevisitStatus

import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationApi
import com.nationalplasticfsm.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}