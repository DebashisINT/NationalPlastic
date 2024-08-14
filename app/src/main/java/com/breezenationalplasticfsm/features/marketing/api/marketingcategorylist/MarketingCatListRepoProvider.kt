package com.breezenationalplasticfsm.features.marketing.api.marketingcategorylist

/**
 * Created by Pratishruti on 28-02-2018.
 */
object MarketingCatListRepoProvider {
    fun provideMarketingCatList(): MarketingCategoryListRepo {
        return MarketingCategoryListRepo(MarketingCategoryListApi.create())
    }
}