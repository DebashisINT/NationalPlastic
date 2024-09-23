package com.breezefieldnationalplastic.features.document.api

import com.breezefieldnationalplastic.features.dymanicSection.api.DynamicApi
import com.breezefieldnationalplastic.features.dymanicSection.api.DynamicRepo

object DocumentRepoProvider {
    fun documentRepoProvider(): DocumentRepo {
        return DocumentRepo(DocumentApi.create())
    }

    fun documentRepoProviderMultipart(): DocumentRepo {
        return DocumentRepo(DocumentApi.createImage())
    }
}