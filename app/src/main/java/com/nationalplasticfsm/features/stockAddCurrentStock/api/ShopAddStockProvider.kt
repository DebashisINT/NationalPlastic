package com.nationalplasticfsm.features.stockAddCurrentStock.api

import com.nationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.nationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusRepository

object ShopAddStockProvider {
    fun provideShopAddStockRepository(): ShopAddStockRepository {
        return ShopAddStockRepository(ShopAddStockApi.create())
    }
}