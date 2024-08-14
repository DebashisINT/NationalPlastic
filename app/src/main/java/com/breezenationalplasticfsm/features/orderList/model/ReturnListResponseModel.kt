package com.breezenationalplasticfsm.features.orderList.model

import com.breezenationalplasticfsm.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}