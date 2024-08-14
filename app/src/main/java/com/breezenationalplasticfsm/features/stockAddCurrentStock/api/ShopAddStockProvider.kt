package com.breezenationalplasticfsm.features.stockAddCurrentStock.api

import com.breezenationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezenationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusRepository

object ShopAddStockProvider {
    fun provideShopAddStockRepository(): ShopAddStockRepository {
        return ShopAddStockRepository(ShopAddStockApi.create())
    }
}