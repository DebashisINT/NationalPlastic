package com.breezenationalplasticfsm.features.document.api

import com.breezenationalplasticfsm.features.dymanicSection.api.DynamicApi
import com.breezenationalplasticfsm.features.dymanicSection.api.DynamicRepo

object DocumentRepoProvider {
    fun documentRepoProvider(): DocumentRepo {
        return DocumentRepo(DocumentApi.create())
    }

    fun documentRepoProviderMultipart(): DocumentRepo {
        return DocumentRepo(DocumentApi.createImage())
    }
}