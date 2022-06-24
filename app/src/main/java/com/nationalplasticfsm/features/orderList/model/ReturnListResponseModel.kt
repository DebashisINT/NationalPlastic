package com.nationalplasticfsm.features.orderList.model

import com.nationalplasticfsm.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}