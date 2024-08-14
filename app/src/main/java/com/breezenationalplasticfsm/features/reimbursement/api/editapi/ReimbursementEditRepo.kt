package com.breezenationalplasticfsm.features.reimbursement.api.editapi

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.reimbursement.model.ApplyReimbursementInputModel
import io.reactivex.Observable

/**
 * Created by Saikat on 08-02-2019.
 */
class ReimbursementEditRepo(val apiService: ReimbursementEditApi) {
    fun editReimbursement(input: ApplyReimbursementInputModel): Observable<BaseResponse> {
        return apiService.editReimbursement(input)
    }
}