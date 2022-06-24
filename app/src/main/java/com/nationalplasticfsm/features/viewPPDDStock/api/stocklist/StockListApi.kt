package com.nationalplasticfsm.features.viewPPDDStock.api.stocklist

import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.features.viewPPDDStock.model.stocklist.StockListResponseModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Saikat on 13-11-2018.
 */
interface StockListApi {

    @FormUrlEncoded
    @POST("StockInfo/StockLists")
    fun stockList(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<StockListResponseModel>

    /**
     * Companion object to create the ShopDurationApi
     */
    companion object Factory {
        fun create(): StockListApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOutNoRetry())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(StockListApi::class.java)
        }
    }
}