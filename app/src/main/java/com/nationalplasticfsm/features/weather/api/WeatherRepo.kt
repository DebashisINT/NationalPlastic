package com.nationalplasticfsm.features.weather.api

import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.task.api.TaskApi
import com.nationalplasticfsm.features.task.model.AddTaskInputModel
import com.nationalplasticfsm.features.weather.model.ForeCastAPIResponse
import com.nationalplasticfsm.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}