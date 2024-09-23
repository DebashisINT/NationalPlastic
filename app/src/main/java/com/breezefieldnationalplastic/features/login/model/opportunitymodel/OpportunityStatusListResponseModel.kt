package com.breezefieldnationalplastic.features.login.model.opportunitymodel

import com.breezefieldnationalplastic.app.domain.OpportunityStatusEntity
import com.breezefieldnationalplastic.app.domain.ProductListEntity
import com.breezefieldnationalplastic.base.BaseResponse

/**
 * Created by Puja on 30.05.2024
 */
class OpportunityStatusListResponseModel : BaseResponse() {
    var status_list: ArrayList<OpportunityStatusEntity>? = null
}