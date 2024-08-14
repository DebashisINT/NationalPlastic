package com.breezenationalplasticfsm.features.lead.api

import com.breezenationalplasticfsm.features.NewQuotation.api.GetQuotListRegRepository
import com.breezenationalplasticfsm.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}