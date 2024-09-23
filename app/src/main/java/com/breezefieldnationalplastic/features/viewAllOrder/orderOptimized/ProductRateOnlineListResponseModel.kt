package com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized

import com.breezefieldnationalplastic.app.domain.ProductOnlineRateTempEntity
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.login.model.productlistmodel.ProductRateDataModel
import java.io.Serializable

class ProductRateOnlineListResponseModel: BaseResponse(), Serializable {
    var product_rate_list: ArrayList<ProductOnlineRateTempEntity>? = null
}