package com.nationalplasticfsm.features.newcollection.model

import com.nationalplasticfsm.app.domain.CollectionDetailsEntity
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}