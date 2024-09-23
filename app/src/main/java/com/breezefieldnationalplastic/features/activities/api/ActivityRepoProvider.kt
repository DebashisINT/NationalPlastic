package com.breezefieldnationalplastic.features.activities.api

import com.breezefieldnationalplastic.features.member.api.TeamApi
import com.breezefieldnationalplastic.features.member.api.TeamRepo

object ActivityRepoProvider {
    fun activityRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.create())
    }

    fun activityImageRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.createImage())
    }
}