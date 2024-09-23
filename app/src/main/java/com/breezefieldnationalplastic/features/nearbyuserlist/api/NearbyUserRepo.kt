package com.breezefieldnationalplastic.features.nearbyuserlist.api

import com.breezefieldnationalplastic.app.Pref
import com.breezefieldnationalplastic.features.nearbyuserlist.model.NearbyUserResponseModel
import com.breezefieldnationalplastic.features.newcollection.model.NewCollectionListResponseModel
import com.breezefieldnationalplastic.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}