package com.breezenationalplasticfsm.features.newcollection.model

import com.breezenationalplasticfsm.app.domain.CollectionDetailsEntity
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}