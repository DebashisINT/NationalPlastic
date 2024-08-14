package com.breezenationalplasticfsm.features.login.model.opportunitymodel

import com.breezenationalplasticfsm.app.domain.OpportunityStatusEntity
import com.breezenationalplasticfsm.app.domain.ProductListEntity
import com.breezenationalplasticfsm.base.BaseResponse

/**
 * Created by Puja on 30.05.2024
 */
class OpportunityStatusListResponseModel : BaseResponse() {
    var status_list: ArrayList<OpportunityStatusEntity>? = null
}