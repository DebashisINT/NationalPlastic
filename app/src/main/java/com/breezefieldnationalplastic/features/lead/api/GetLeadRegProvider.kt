package com.breezefieldnationalplastic.features.lead.api

import com.breezefieldnationalplastic.features.NewQuotation.api.GetQuotListRegRepository
import com.breezefieldnationalplastic.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}