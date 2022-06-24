package com.nationalplasticfsm.features.document.api

import com.nationalplasticfsm.features.dymanicSection.api.DynamicApi
import com.nationalplasticfsm.features.dymanicSection.api.DynamicRepo

object DocumentRepoProvider {
    fun documentRepoProvider(): DocumentRepo {
        return DocumentRepo(DocumentApi.create())
    }

    fun documentRepoProviderMultipart(): DocumentRepo {
        return DocumentRepo(DocumentApi.createImage())
    }
}