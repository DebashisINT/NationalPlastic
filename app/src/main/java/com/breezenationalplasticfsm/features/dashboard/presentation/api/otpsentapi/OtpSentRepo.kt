package com.breezenationalplasticfsm.features.dashboard.presentation.api.otpsentapi

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.base.BaseResponse
import io.reactivex.Observable

/**
 * Created by Saikat on 22-11-2018.
 */
class OtpSentRepo(val apiService: OtpSentAPi) {
    fun otpSent(shopId: String): Observable<BaseResponse> {
        return apiService.otpSent(Pref.session_token!!, Pref.user_id!!, shopId)
    }
}