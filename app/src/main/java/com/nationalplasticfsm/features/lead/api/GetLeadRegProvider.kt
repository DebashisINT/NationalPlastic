package com.nationalplasticfsm.features.lead.api

import com.nationalplasticfsm.features.NewQuotation.api.GetQuotListRegRepository
import com.nationalplasticfsm.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}