package com.nationalplasticfsm.features.nearbyuserlist.api

import com.nationalplasticfsm.app.Pref
import com.nationalplasticfsm.features.nearbyuserlist.model.NearbyUserResponseModel
import com.nationalplasticfsm.features.newcollection.model.NewCollectionListResponseModel
import com.nationalplasticfsm.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}