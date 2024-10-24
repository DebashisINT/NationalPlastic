package com.breezefieldnationalplastic.features.viewAllOrder.api

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.login.model.productlistmodel.NewOdrScrOrderListModel
import com.breezefieldnationalplastic.features.viewAllOrder.OrdResponse
import com.breezefieldnationalplastic.features.viewAllOrder.model.NewOrderDataModel
import com.breezefieldnationalplastic.features.viewAllOrder.model.NewOrderOrderHistoryModel
import com.breezefieldnationalplastic.features.viewAllOrder.model.ViewAllOrderListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 01-10-2018.
 */
class OrderDetailsListRepo(val apiService: OrderDetailsListApi) {
    fun getOrderDetailsList(sessiontoken: String, user_id: String, shop_id: String, order_id: String): Observable<ViewAllOrderListResponseModel> {
        return apiService.getOrderDetailsList(sessiontoken, user_id, shop_id, order_id)
    }

    fun getNewOrderData(): Observable<NewOrderDataModel> {
        return apiService.getNewOrderData(Pref.session_token!!, Pref.user_id!!)
    }

    fun getNewOrderHistoryData(): Observable<NewOrderOrderHistoryModel> {
        return apiService.getNewOrderHistoryData(Pref.session_token!!, Pref.user_id!!)
    }

    fun getNewOrderHistoryDataSimplefied(): Observable<NewOdrScrOrderListModel> {
        return apiService.getNewOrderHistoryDataSimplefied(Pref.session_token!!, Pref.user_id!!)
    }

    fun getOrderStatusL(): Observable<OrdResponse> {
        return apiService.getOrderStatusLApi(Pref.user_id!!)
    }

}