package com.nationalplasticfsm.features.orderList.api.neworderlistapi

import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.login.model.ShopFeedbackResponseModel
import com.nationalplasticfsm.features.orderList.model.NewOrderListResponseModel
import com.nationalplasticfsm.features.orderList.model.ReturnListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 03-12-2018.
 */
class NewOrderListRepo(val apiService: NewOrderListApi) {
    fun getOrderList(sessiontoken: String, user_id: String, date: String): Observable<NewOrderListResponseModel> {
        return apiService.getOrderList(sessiontoken, user_id, date)
    }

    fun getReturnList(sessiontoken: String, user_id: String, date: String): Observable<ReturnListResponseModel> {
        return apiService.getReturnList(sessiontoken, user_id, date)
    }

    fun getShopFeedback(user_id: String,from_date:String,to_date : String,date_span:String): Observable<ShopFeedbackResponseModel> {
        return apiService.getShopFeedbackApi(user_id,from_date,to_date,date_span)
    }

    fun sendOrderEmail(shopId: String, orderId: String, type: String): Observable<BaseResponse> {
        return apiService.sendOrderEmail(Pref.session_token!!, Pref.user_id!!, orderId, shopId, type)
    }
}