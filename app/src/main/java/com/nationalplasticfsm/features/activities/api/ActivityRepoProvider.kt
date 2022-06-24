package com.nationalplasticfsm.features.activities.api

import com.nationalplasticfsm.features.member.api.TeamApi
import com.nationalplasticfsm.features.member.api.TeamRepo

object ActivityRepoProvider {
    fun activityRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.create())
    }

    fun activityImageRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.createImage())
    }
}