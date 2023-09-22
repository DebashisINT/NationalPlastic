package com.nationalplasticfsm.features.shopdetail.presentation.api

import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.addshop.model.AddShopRequestData
import com.nationalplasticfsm.features.addshop.model.AddShopResponse
import com.nationalplasticfsm.features.addshop.model.LogFileResponse
import com.nationalplasticfsm.features.login.model.WhatsappApiData
import com.nationalplasticfsm.features.login.model.WhatsappApiFetchData
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Saikat on 10-10-2018.
 */
interface EditShopApi {

    @POST("Shoplist/EditShop")
    fun editShop(@Body addShop: AddShopRequestData?): Observable<AddShopResponse>

    @POST("WhatsAppMessageInfo/WhatsAppMsgSave")
    fun whatsAppStatusSyncApi(@Body addShop: WhatsappApiData?): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("WhatsAppMessageInfo/WhatsAppMsgList")
    fun whatsAppStatusFetchApi(@Field("user_id") user_id: String): Observable<WhatsappApiFetchData>

    @Multipart
    @POST("ShopRegistration/NewShopEdit")
    fun editShopWithDegImage(@Query("data") addShop: String, @Part deg_img_data: MultipartBody.Part?): Observable<AddShopResponse>


    @Multipart
    @POST("ShopRegistration/EditShop")
    fun editShopWithImage(@Query("data") addShop: String, @Part logo_img_data: MultipartBody.Part?): Observable<AddShopResponse>

    @Multipart
    @POST("APPLogFilesDetection/APPLogFilesSave")
    fun logshareFile(@Query("data") userId: String, @Part attachments: MultipartBody.Part?): Observable<LogFileResponse>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): EditShopApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                    .build()

            return retrofit.create(EditShopApi::class.java)
        }

        fun createWithoutMultipart(): EditShopApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(EditShopApi::class.java)
        }
    }
}