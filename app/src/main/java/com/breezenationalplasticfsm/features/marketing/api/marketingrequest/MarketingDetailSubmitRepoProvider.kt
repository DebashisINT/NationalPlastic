package com.breezenationalplasticfsm.features.marketing.api.marketingrequest

/**
 * Created by Pratishruti on 02-03-2018.
 */
object MarketingDetailSubmitRepoProvider {
    fun providesMarketingDetailsSubmit(): MarketingDetailSubmitRepo {
        return MarketingDetailSubmitRepo(MarketingDetailSubmitApi.create())
    }
}