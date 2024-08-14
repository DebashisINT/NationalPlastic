package com.breezenationalplasticfsm.features.stockCompetetorStock.api

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.orderList.model.NewOrderListResponseModel
import com.breezenationalplasticfsm.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.breezenationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class AddCompStockRepository(val apiService:AddCompStockApi){

    fun addCompStock(shopAddCompetetorStockRequest: ShopAddCompetetorStockRequest): Observable<BaseResponse> {
        return apiService.submShopCompStock(shopAddCompetetorStockRequest)
    }

    fun getCompStockList(sessiontoken: String, user_id: String, date: String): Observable<CompetetorStockGetData> {
        return apiService.getCompStockList(sessiontoken, user_id, date)
    }
}