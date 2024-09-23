package com.breezefieldnationalplastic.features.billing.api.billinglistapi

import com.breezefieldnationalplastic.features.billing.model.BillingListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 20-02-2019.
 */
class BillingListRepo(val apiService: BillingListApi) {
    fun getBillList(sessiontoken: String, user_id: String, order_id: String): Observable<BillingListResponseModel> {
        return apiService.getBillList(sessiontoken, user_id, order_id)
    }
}