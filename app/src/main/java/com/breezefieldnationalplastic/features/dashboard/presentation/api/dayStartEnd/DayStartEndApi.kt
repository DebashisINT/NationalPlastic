package com.breezefieldnationalplastic.features.dashboard.presentation.api.dayStartEnd

import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.dashboard.presentation.model.DaystartDayendRequest
import com.breezefieldnationalplastic.features.dashboard.presentation.model.StatusDayStartEnd
import com.breezefieldnationalplastic.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.breezefieldnationalplastic.features.stockCompetetorStock.api.AddCompStockApi
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DayStartEndApi {

    @POST("UserWiseDayStartEnd/UserDayStartEnd")
    fun submitDayStartEnd(@Body daystartDayendRequest: DaystartDayendRequest?): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("UserWiseDayStartEnd/UserStatusDayStartEnd")
    fun statusDayStartEnd(@Field("session_token") session_token: String, @Field("user_id") user_id: String,@Field("date") date: String):
            Observable<StatusDayStartEnd>

    @FormUrlEncoded
    @POST("UserWiseDayStartEnd/UserDayStartEndDelete")
    fun submitDayStartEndDelApi(@Field("session_token") session_token: String,@Field("user_id") user_id: String,
                                @Field("date") date: String,@Field("isdaystartdel") isdaystartdel: String,
                                @Field("isdayenddel") isdayenddel: String,): Observable<BaseResponse>

    companion object Factory {
        fun create(): DayStartEndApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOutNoRetry())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(DayStartEndApi::class.java)
        }
    }

}