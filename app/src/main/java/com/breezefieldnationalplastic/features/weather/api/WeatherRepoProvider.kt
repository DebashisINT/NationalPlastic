package com.breezefieldnationalplastic.features.weather.api

import com.breezefieldnationalplastic.features.task.api.TaskApi
import com.breezefieldnationalplastic.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}