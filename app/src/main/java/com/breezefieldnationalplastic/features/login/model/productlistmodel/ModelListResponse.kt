package com.breezefieldnationalplastic.features.login.model.productlistmodel

import com.breezefieldnationalplastic.app.domain.ModelEntity
import com.breezefieldnationalplastic.app.domain.ProductListEntity
import com.breezefieldnationalplastic.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}