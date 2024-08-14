package com.breezenationalplasticfsm.features.addAttendence.api

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.features.addAttendence.model.WorkTypeResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 31-08-2018.
 */
class WorkTypeListRepo(val apiService: WorkTypeListApi) {
    fun getWorkTypeList(): Observable<WorkTypeResponseModel> {
        return apiService.getWorkType(Pref.user_id!!)
    }
}