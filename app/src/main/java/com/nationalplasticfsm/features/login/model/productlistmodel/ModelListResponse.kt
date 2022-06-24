package com.nationalplasticfsm.features.login.model.productlistmodel

import com.nationalplasticfsm.app.domain.ModelEntity
import com.nationalplasticfsm.app.domain.ProductListEntity
import com.nationalplasticfsm.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}