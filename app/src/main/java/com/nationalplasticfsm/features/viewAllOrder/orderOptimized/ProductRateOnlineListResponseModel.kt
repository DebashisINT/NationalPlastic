package com.nationalplasticfsm.features.viewAllOrder.orderOptimized

import com.nationalplasticfsm.app.domain.ProductOnlineRateTempEntity
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.login.model.productlistmodel.ProductRateDataModel
import java.io.Serializable

class ProductRateOnlineListResponseModel: BaseResponse(), Serializable {
    var product_rate_list: ArrayList<ProductOnlineRateTempEntity>? = null
}