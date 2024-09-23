package com.breezefieldnationalplastic.features.weather.api

import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.task.api.TaskApi
import com.breezefieldnationalplastic.features.task.model.AddTaskInputModel
import com.breezefieldnationalplastic.features.weather.model.ForeCastAPIResponse
import com.breezefieldnationalplastic.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}