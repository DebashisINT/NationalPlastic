package com.breezenationalplasticfsm.features.location.ideallocapi

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.location.model.IdealLocationInputParams
import io.reactivex.Observable

/**
 * Created by Saikat on 05-02-2019.
 */
class IdealLocationRepo(val apiService: IdealLocationApi) {
    fun idealLocation(idealLoc: IdealLocationInputParams?): Observable<BaseResponse> {
        return apiService.submitIdealLocation(idealLoc)
    }
}