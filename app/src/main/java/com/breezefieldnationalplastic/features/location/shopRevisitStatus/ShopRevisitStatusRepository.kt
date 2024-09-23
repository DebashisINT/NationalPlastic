package com.breezefieldnationalplastic.features.location.shopRevisitStatus

import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.location.model.ShopDurationRequest
import com.breezefieldnationalplastic.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}