package com.nationalplasticfsm.features.stockAddCurrentStock.api

import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.location.model.ShopRevisitStatusRequest
import com.nationalplasticfsm.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.nationalplasticfsm.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.nationalplasticfsm.features.stockAddCurrentStock.model.CurrentStockGetData
import com.nationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}