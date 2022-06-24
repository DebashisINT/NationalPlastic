package com.nationalplasticfsm.features.weather.api

import com.nationalplasticfsm.features.task.api.TaskApi
import com.nationalplasticfsm.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}