package com.breezenationalplasticfsm.features.location.shopRevisitStatus

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.location.model.ShopDurationRequest
import com.breezenationalplasticfsm.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}