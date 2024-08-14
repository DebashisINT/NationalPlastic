package com.breezenationalplasticfsm.features.login.model.productlistmodel

import com.breezenationalplasticfsm.app.domain.ModelEntity
import com.breezenationalplasticfsm.app.domain.ProductListEntity
import com.breezenationalplasticfsm.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}