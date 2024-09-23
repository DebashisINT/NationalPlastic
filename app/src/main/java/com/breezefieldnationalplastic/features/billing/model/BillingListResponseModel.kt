package com.breezefieldnationalplastic.features.billing.model

import com.breezefieldnationalplastic.base.BaseResponse

/**
 * Created by Saikat on 20-02-2019.
 */
class BillingListResponseModel : BaseResponse() {
    var billing_list: ArrayList<BillingListDataModel>? = null
}