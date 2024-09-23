package com.breezefieldnationalplastic.features.dashboard.presentation.api.unreadnotificationapi

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.dashboard.presentation.model.UnreadNotificationResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 07-03-2019.
 */
class UnreadNotificationRepo(val apiService: UnreadNotificationApi) {
    fun unreadNotification(): Observable<UnreadNotificationResponseModel> {
        return apiService.unreadNotification(Pref.session_token!!, Pref.user_id!!)
    }
}