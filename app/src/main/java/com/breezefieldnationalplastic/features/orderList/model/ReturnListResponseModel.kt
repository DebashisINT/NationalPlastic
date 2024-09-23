package com.breezefieldnationalplastic.features.orderList.model

import com.breezefieldnationalplastic.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}