package com.breezenationalplasticfsm.features.weather.api

import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.task.api.TaskApi
import com.breezenationalplasticfsm.features.task.model.AddTaskInputModel
import com.breezenationalplasticfsm.features.weather.model.ForeCastAPIResponse
import com.breezenationalplasticfsm.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}