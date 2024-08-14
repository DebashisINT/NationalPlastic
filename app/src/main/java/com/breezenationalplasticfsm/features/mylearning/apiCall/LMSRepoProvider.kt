package com.breezenationalplasticfsm.features.mylearning.apiCall

import com.breezenationalplasticfsm.features.login.api.opportunity.OpportunityListApi
import com.breezenationalplasticfsm.features.login.api.opportunity.OpportunityListRepo

object LMSRepoProvider {
    fun getTopicList(): LMSRepo {
        return LMSRepo(LMSApi.create())
    }
}