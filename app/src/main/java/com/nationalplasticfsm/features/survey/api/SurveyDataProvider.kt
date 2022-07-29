package com.nationalplasticfsm.features.survey.api

import com.nationalplasticfsm.features.photoReg.api.GetUserListPhotoRegApi
import com.nationalplasticfsm.features.photoReg.api.GetUserListPhotoRegRepository

object SurveyDataProvider{

    fun provideSurveyQ(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.create())
    }

    fun provideSurveyQMultiP(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.createImage())
    }
}