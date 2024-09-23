package com.breezefieldnationalplastic.features.survey.api

import com.breezefieldnationalplastic.features.photoReg.api.GetUserListPhotoRegApi
import com.breezefieldnationalplastic.features.photoReg.api.GetUserListPhotoRegRepository

object SurveyDataProvider{

    fun provideSurveyQ(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.create())
    }

    fun provideSurveyQMultiP(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.createImage())
    }
}