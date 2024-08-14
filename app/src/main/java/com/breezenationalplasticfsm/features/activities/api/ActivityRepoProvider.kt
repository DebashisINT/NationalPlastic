package com.breezenationalplasticfsm.features.activities.api

import com.breezenationalplasticfsm.features.member.api.TeamApi
import com.breezenationalplasticfsm.features.member.api.TeamRepo

object ActivityRepoProvider {
    fun activityRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.create())
    }

    fun activityImageRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.createImage())
    }
}