package com.breezenationalplasticfsm.features.survey.api

import com.breezenationalplasticfsm.features.photoReg.api.GetUserListPhotoRegApi
import com.breezenationalplasticfsm.features.photoReg.api.GetUserListPhotoRegRepository

object SurveyDataProvider{

    fun provideSurveyQ(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.create())
    }

    fun provideSurveyQMultiP(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.createImage())
    }
}