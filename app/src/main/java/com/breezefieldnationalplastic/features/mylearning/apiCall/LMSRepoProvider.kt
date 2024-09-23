package com.breezefieldnationalplastic.features.mylearning.apiCall

import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityListApi
import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityListRepo

object LMSRepoProvider {
    fun getTopicList(): LMSRepo {
        return LMSRepo(LMSApi.create())
    }
}