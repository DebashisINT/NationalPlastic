package com.breezefieldnationalplastic.features.login.api.opportunity

/**
 * Created by Saikat on 20-11-2018.
 */
object OpportunityRepoProvider {
    fun opportunityListRepo(): OpportunityListRepo {
        return OpportunityListRepo(OpportunityListApi.create())
    }
}