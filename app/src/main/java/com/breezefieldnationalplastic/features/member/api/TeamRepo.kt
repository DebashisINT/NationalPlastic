package com.breezefieldnationalplastic.features.member.api

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.contacts.TeamAllListRes
import com.breezefieldnationalplastic.features.contacts.TeamListRes
import com.breezefieldnationalplastic.features.member.model.*
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by Saikat on 29-01-2020.
 */
class TeamRepo(val apiService: TeamApi) {
    fun teamList(userId: String, isFirstScreen: Boolean, isAllTeam: Boolean): Observable<TeamListResponseModel> {
        Timber.d("PJP api teamList call")
        return apiService.getTeamList(Pref.session_token!!, userId, isFirstScreen, isAllTeam)
    }

    fun teamListNew(userId: String, isFirstScreen: Boolean, isAllTeam: Boolean): Observable<TeamListRes> {
        Timber.d("PJP api teamList call")
        return apiService.getTeamListNew(Pref.session_token!!, userId, isFirstScreen, isAllTeam)
    }

    fun teamAllListNew(userId: String, isFirstScreen: Boolean, isAllTeam: Boolean): Observable<TeamAllListRes> {
        Timber.d("PJP api teamList call")
        return apiService.getTeamAllListNew(Pref.session_token!!, userId, isFirstScreen, isAllTeam)
    }

    fun teamShopList(userId: String, areaId: String): Observable<TeamShopListResponseModel> {
        Timber.d("PJP api teamShopList call")
        return apiService.getTeamShopList(Pref.session_token!!, userId, areaId)
    }

    fun teamAllShopList(userId: String, shopId: String, areaId: String): Observable<TeamShopListResponseModel> {
        Timber.d("PJP api teamAllShopList call")
        return apiService.getAllTeamShopList(Pref.session_token!!, userId, shopId, areaId)
    }

    fun teamLocList(userId: String, date: String): Observable<TeamLocListResponseModel> {
        Timber.d("PJP api PJPDetails/TeamLocationList call")
        return apiService.getTeamLocList(Pref.session_token!!, userId, Pref.user_id!!, date)
    }

    fun teamPjpList(userId: String, year: String, month: String): Observable<TeamPjpResponseModel> {
        Timber.d("PJP api PJPDetails/PJPDetailsList call")
        return apiService.getTeamPJPList(Pref.session_token!!, userId, Pref.user_id!!, year, month)
    }

    fun teamPjpConfig(userId: String): Observable<TeamPjpConfigResponseModel> {
        Timber.d("PJP api PJPDetails/PJPConfigList call")
        return apiService.getTeamPJPConfig(Pref.session_token!!, userId, Pref.user_id!!)
    }

    fun teamCustomerList(userId: String): Observable<CustomerResponseModel> {
        Timber.d("PJP api PJPDetails/PJPCustomer call")
        return apiService.getCustomerList(Pref.session_token!!, userId, Pref.user_id!!)
    }

    fun addPjp(addPjpInputParams: AddpjpInputParams): Observable<BaseResponse> {
        Timber.d("PJP api PJPDetails/PJPAddList call")
        return apiService.addPjp(addPjpInputParams)
    }

    fun deletePjp(userId: String, pjpId: String): Observable<BaseResponse> {
        Timber.d("PJP api PJPDetails/PJPDeleteList call")
        return apiService.deletePJP(Pref.session_token!!, userId, Pref.user_id!!, pjpId)
    }

    fun editPjp(editPjpInputParams: EditPjpInputParams): Observable<BaseResponse> {
        Timber.d("PJP api PJPDetails/PJPEditList call")
        return apiService.editPjp(editPjpInputParams)
    }

    fun getUserPJPList(date: String): Observable<UserPjpResponseModel> {
        Timber.d("PJP api PJPDetails/PJPList call")
        return apiService.getUserPJPList(Pref.session_token!!, Pref.user_id!!, date)
    }

    fun offlineTeamList(date: String): Observable<TeamListResponseModel> {
        Timber.d("PJP api OfflineTeam/GetMemberList call")
        return apiService.getOfflineTeamList(Pref.session_token!!, Pref.user_id!!, date)
    }

    fun offlineTeamShopList(date: String): Observable<TeamShopListResponseModel> {
        Timber.d("PJP api OfflineTeam/GetShopList call")
        return apiService.getOfflineTeamShopList(Pref.session_token!!, Pref.user_id!!, date)
    }

    fun teamAreaList(): Observable<TeamAreaListResponseModel> {
        Timber.d("PJP api OfflineTeam/GetAreaList call")
        return apiService.getOfflineAreaList(Pref.session_token!!, Pref.user_id!!, Pref.profile_city)
    }
}