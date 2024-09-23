package com.breezefieldnationalplastic.features.dashboard.presentation.api.gteroutelistapi

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.dashboard.presentation.model.SelectedRouteListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 03-12-2018.
 */
class GetRouteListRepo(val apiService: GetRouteListApi) {
    fun routeList(): Observable<SelectedRouteListResponseModel> {
        return apiService.getRouteList(Pref.session_token!!, Pref.user_id!!)
    }
}