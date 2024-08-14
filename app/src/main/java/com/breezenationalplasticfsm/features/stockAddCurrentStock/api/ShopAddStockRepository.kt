package com.breezenationalplasticfsm.features.stockAddCurrentStock.api

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.location.model.ShopRevisitStatusRequest
import com.breezenationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezenationalplasticfsm.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.breezenationalplasticfsm.features.stockAddCurrentStock.model.CurrentStockGetData
import com.breezenationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}