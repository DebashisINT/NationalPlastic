package com.breezefieldnationalplastic.features.stockCompetetorStock.api

import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.orderList.model.NewOrderListResponseModel
import com.breezefieldnationalplastic.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.breezefieldnationalplastic.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class AddCompStockRepository(val apiService:AddCompStockApi){

    fun addCompStock(shopAddCompetetorStockRequest: ShopAddCompetetorStockRequest): Observable<BaseResponse> {
        return apiService.submShopCompStock(shopAddCompetetorStockRequest)
    }

    fun getCompStockList(sessiontoken: String, user_id: String, date: String): Observable<CompetetorStockGetData> {
        return apiService.getCompStockList(sessiontoken, user_id, date)
    }
}