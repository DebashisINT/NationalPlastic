package com.breezenationalplasticfsm.features.nearbyuserlist.api

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.features.nearbyuserlist.model.NearbyUserResponseModel
import com.breezenationalplasticfsm.features.newcollection.model.NewCollectionListResponseModel
import com.breezenationalplasticfsm.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}