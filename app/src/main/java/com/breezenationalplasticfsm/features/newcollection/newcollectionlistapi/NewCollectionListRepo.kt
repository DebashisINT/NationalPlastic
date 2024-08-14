package com.breezenationalplasticfsm.features.newcollection.newcollectionlistapi

import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.features.newcollection.model.CollectionDetailsResponseModel
import com.breezenationalplasticfsm.features.newcollection.model.CollectionShopListResponseModel
import com.breezenationalplasticfsm.features.newcollection.model.NewCollectionListResponseModel
import com.breezenationalplasticfsm.features.newcollection.model.PaymentModeResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListRepo(val apiService: NewCollectionListApi) {
    fun collectionList(session_token: String, user_id: String, date: String): Observable<NewCollectionListResponseModel> {
        return apiService.newCollectionList(session_token, user_id, date)
    }

    fun collectionDetails(): Observable<CollectionDetailsResponseModel> {
        return apiService.newCollectionDetails(Pref.session_token!!, Pref.user_id!!)
    }

    fun collectionShopList(date: String): Observable<CollectionShopListResponseModel> {
        return apiService.newCollectionShopList(Pref.session_token!!, Pref.user_id!!, date)
    }

    fun paymentModeList(): Observable<PaymentModeResponseModel> {
        return apiService.paymentModeList(Pref.session_token!!, Pref.user_id!!)
    }
}