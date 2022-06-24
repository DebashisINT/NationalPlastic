package com.nationalplasticfsm.features.location.shopRevisitStatus

import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.location.model.ShopDurationRequest
import com.nationalplasticfsm.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}