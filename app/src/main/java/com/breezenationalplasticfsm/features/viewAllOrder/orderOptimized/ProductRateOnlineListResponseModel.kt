package com.breezenationalplasticfsm.features.viewAllOrder.orderOptimized

import com.breezenationalplasticfsm.app.domain.ProductOnlineRateTempEntity
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductRateDataModel
import java.io.Serializable

class ProductRateOnlineListResponseModel: BaseResponse(), Serializable {
    var product_rate_list: ArrayList<ProductOnlineRateTempEntity>? = null
}