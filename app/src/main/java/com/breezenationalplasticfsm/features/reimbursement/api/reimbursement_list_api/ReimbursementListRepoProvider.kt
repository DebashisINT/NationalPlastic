package com.breezenationalplasticfsm.features.reimbursement.api.reimbursement_list_api

/**
 * Created by Saikat on 25-01-2019.
 */
object ReimbursementListRepoProvider {
    fun getReimbursementListRepository(): ReimbursementListRepo {
        return ReimbursementListRepo(ReimbursementListApi.create())
    }
}