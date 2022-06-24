package com.nationalplasticfsm.features.reimbursement.api.reimbursement_list_api

import com.nationalplasticfsm.features.reimbursement.model.reimbursementlist.ReimbursementListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 25-01-2019.
 */
class ReimbursementListRepo(val apiService: ReimbursementListApi) {
    fun getReimbursementList(user_id: String, session_token: String, month: String, year: String, visit_type: String): Observable<ReimbursementListResponseModel> {
        return apiService.getReimbursementList(user_id, session_token, month, year, visit_type)
    }
}