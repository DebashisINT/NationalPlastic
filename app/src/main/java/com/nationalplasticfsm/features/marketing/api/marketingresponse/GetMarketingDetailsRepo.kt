package com.nationalplasticfsm.features.marketing.api.marketingresponse

import com.nationalplasticfsm.features.marketing.model.GetMarketingDetailsResponse
import io.reactivex.Observable

/**
 * Created by Pratishruti on 28-02-2018.
 */
class GetMarketingDetailsRepo(val apiService: GetMarketingDetailsApi) {
    fun getMarketingDetails(shop_id:String,user_id:String): Observable<GetMarketingDetailsResponse> {
        return apiService.getMarketingDetails(shop_id,user_id)
    }
}