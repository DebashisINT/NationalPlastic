package com.breezenationalplasticfsm.features.marketing.api.marketingcategorylist

import com.breezenationalplasticfsm.features.marketing.model.MarketingCategoryListResponse
import io.reactivex.Observable

/**
 * Created by Pratishruti on 28-02-2018.
 */
class MarketingCategoryListRepo(val apiService: MarketingCategoryListApi) {
    fun getMarketingCategoryList(): Observable<MarketingCategoryListResponse> {
        return apiService.getMarketingCategoryList()
    }
}