package com.breezenationalplasticfsm.features.reimbursement.model.reimbursementlist

import com.breezenationalplasticfsm.base.BaseResponse

/**
 * Created by Saikat on 28-01-2019.
 */
class ReimbursementListResponseModel : BaseResponse() {
    var total_claim_amount: String? = null
    var total_approved_amount: String? = null
    var expense_list: ArrayList<ReimbursementListDataModel>? = null
}