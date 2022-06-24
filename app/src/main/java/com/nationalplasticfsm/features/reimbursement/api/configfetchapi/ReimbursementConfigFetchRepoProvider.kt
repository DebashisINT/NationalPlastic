package com.nationalplasticfsm.features.reimbursement.api.configfetchapi

/**
 * Created by Saikat on 24-01-2019.
 */
object ReimbursementConfigFetchRepoProvider {
    fun provideFetchReimbursementConfigRepository(): ReimbursementConfigFetchRepo {
        return ReimbursementConfigFetchRepo(ReimbursementConfigFetchApi.create())
    }
}