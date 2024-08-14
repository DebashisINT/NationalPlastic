package com.breezenationalplasticfsm.features.login.api.opportunity

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.app.utils.AppUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.addshop.model.AudioFetchDataCLass
import com.breezenationalplasticfsm.features.login.model.opportunitymodel.OpportunityStatusListResponseModel
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListResponseModel
import com.breezenationalplasticfsm.features.orderITC.SyncDeleteOppt
import com.breezenationalplasticfsm.features.orderITC.SyncEditOppt
import com.breezenationalplasticfsm.features.orderITC.SyncOppt
import com.breezenationalplasticfsm.features.orderList.model.OpportunityListResponseModel
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by Saikat on 20-11-2018.
 */
class OpportunityListRepo(val apiService: OpportunityListApi) {
    fun getOpportunityStatus(session_token: String): Observable<OpportunityStatusListResponseModel> {
        return apiService.getOpportunityStatusList(session_token)
    }

    fun saveOpportunity(syncOppt: SyncOppt): Observable<BaseResponse> {
        return apiService.saveOpportunity(syncOppt)
    }

    fun editOpportunity(syncEditOppt: SyncEditOppt): Observable<BaseResponse> {
        return apiService.editOpportunity(syncEditOppt)
    }
    fun deleteOpportunity(syncDeleteOppt: SyncDeleteOppt): Observable<BaseResponse> {
        return apiService.deleteOpportunity(syncDeleteOppt)
    }
    fun getOpportunityL(user_id: String): Observable<OpportunityListResponseModel> {
        return apiService.getOpportunityL(user_id)
    }

    fun getAudioL(user_id: String,data_limit_in_days:String): Observable<AudioFetchDataCLass> {
        return apiService.getAudioLApi(user_id,data_limit_in_days)
    }
}