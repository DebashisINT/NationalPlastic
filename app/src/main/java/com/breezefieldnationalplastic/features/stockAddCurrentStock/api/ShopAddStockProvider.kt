package com.breezefieldnationalplastic.features.stockAddCurrentStock.api

import com.breezefieldnationalplastic.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezefieldnationalplastic.features.location.shopRevisitStatus.ShopRevisitStatusRepository

object ShopAddStockProvider {
    fun provideShopAddStockRepository(): ShopAddStockRepository {
        return ShopAddStockRepository(ShopAddStockApi.create())
    }
}