package com.nationalplasticfsm.features.stockCompetetorStock.api

import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.orderList.model.NewOrderListResponseModel
import com.nationalplasticfsm.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.nationalplasticfsm.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class AddCompStockRepository(val apiService:AddCompStockApi){

    fun addCompStock(shopAddCompetetorStockRequest: ShopAddCompetetorStockRequest): Observable<BaseResponse> {
        return apiService.submShopCompStock(shopAddCompetetorStockRequest)
    }

    fun getCompStockList(sessiontoken: String, user_id: String, date: String): Observable<CompetetorStockGetData> {
        return apiService.getCompStockList(sessiontoken, user_id, date)
    }
}