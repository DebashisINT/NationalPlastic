package com.breezefieldnationalplastic.features.location.shopRevisitStatus

import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationApi
import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}