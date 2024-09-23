package com.breezefieldnationalplastic.features.stockAddCurrentStock.api

import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.location.model.ShopRevisitStatusRequest
import com.breezefieldnationalplastic.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezefieldnationalplastic.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.breezefieldnationalplastic.features.stockAddCurrentStock.model.CurrentStockGetData
import com.breezefieldnationalplastic.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}