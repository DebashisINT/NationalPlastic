package com.breezenationalplasticfsm.features.weather.api

import com.breezenationalplasticfsm.features.task.api.TaskApi
import com.breezenationalplasticfsm.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}