package com.breezefieldnationalplastic.features.addshop.api.areaList

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.addshop.model.AreaListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 14-May-20.
 */
class AreaListRepo(val apiService: AreaListApi) {
    fun areaList(city_id: String, userId: String): Observable<AreaListResponseModel> {
        return apiService.getAreaList(Pref.session_token!!, userId, city_id, Pref.user_id!!)
    }
}