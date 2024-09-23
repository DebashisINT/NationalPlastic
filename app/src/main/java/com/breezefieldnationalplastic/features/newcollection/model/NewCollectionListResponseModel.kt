package com.breezefieldnationalplastic.features.newcollection.model

import com.breezefieldnationalplastic.app.domain.CollectionDetailsEntity
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}