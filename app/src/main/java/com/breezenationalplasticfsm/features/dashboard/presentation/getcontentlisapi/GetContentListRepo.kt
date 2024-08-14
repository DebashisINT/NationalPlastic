package com.breezenationalplasticfsm.features.dashboard.presentation.getcontentlisapi

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.dashboard.presentation.model.ContentListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 05-03-2019.
 */
class GetContentListRepo(val apiService: GetContentListApi) {
    fun getContentList(): Observable<ContentListResponseModel> {
        return apiService.getContentList(Pref.session_token!!)
    }

    fun changePassword(old_pwd: String, new_pwd: String): Observable<BaseResponse> {
        return apiService.changePassword(Pref.session_token!!, Pref.user_id!!, old_pwd, new_pwd)
    }
}