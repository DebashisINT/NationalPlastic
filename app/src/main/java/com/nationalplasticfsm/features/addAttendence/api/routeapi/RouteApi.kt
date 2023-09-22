package com.nationalplasticfsm.features.addAttendence.api.routeapi

import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.features.addAttendence.model.AreaListResponse
import com.nationalplasticfsm.features.addAttendence.model.DistanceResponseModel
import com.nationalplasticfsm.features.addAttendence.model.LocationListResponseModel
import com.nationalplasticfsm.features.addAttendence.model.RouteResponseModel
import com.nationalplasticfsm.features.addAttendence.model.VisitLocationListResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Saikat on 22-11-2018.
 */
interface RouteApi {

    @FormUrlEncoded
    @POST("RouteList/List")
    fun getRouteList(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<RouteResponseModel>

    @FormUrlEncoded
    @POST("Area/AreaList")
    fun getLocationList(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<LocationListResponseModel>

    @FormUrlEncoded
    @POST("Area/AreaListByCity")
    fun getAreaList(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("city_id") city_id: String): Observable<AreaListResponse>

    @FormUrlEncoded
    @POST("LocationAddModify/VisitLocationList")
    fun getVisitLocationList(@Field("session_token") session_token: String): Observable<VisitLocationListResponse>


    @FormUrlEncoded
    @POST("Area/DistanceList")
    fun getDistance(@Field("session_token") session_token: String, @Field("user_id") user_id: String,
                    @Field("from_id") from_id: String, @Field("to_id") to_id: String): Observable<DistanceResponseModel>


    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): RouteApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(RouteApi::class.java)
        }
    }
}