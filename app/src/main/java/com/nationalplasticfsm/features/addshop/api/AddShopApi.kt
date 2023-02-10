package com.nationalplasticfsm.features.addshop.api

import com.nationalplasticfsm.app.NetworkConstant
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.addshop.model.AddQuestionSubmitRequestData
import com.nationalplasticfsm.features.addshop.model.AddShopRequestData
import com.nationalplasticfsm.features.addshop.model.AddShopResponse
import com.nationalplasticfsm.features.addshop.model.imageListResponse
import com.nationalplasticfsm.features.addshop.presentation.ShopListSubmitResponse
import com.nationalplasticfsm.features.addshop.presentation.multiContactRequestData
import com.nationalplasticfsm.features.beatCustom.BeatGetStatusModel
import com.nationalplasticfsm.features.nearbyshops.presentation.ShopModifiedListResponse
import com.nationalplasticfsm.features.nearbyshops.presentation.ShopModifiedUpdateList
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Pratishruti on 22-11-2017.
 */
interface AddShopApi {


    @POST("RubyFoodLead/QuestionListSave")
    fun getAddQuestionSubmit(@Body addQuestion:AddQuestionSubmitRequestData?): Observable<BaseResponse>

    // 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList  for shop update mantis 25624
    @FormUrlEncoded
    @POST("Shoplist/ModifiedShopLists")
    fun getModifiedShopList(@Field("user_id") user_id: String,@Field("session_token") session_token: String): Observable<ShopModifiedListResponse>

    // 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList  for shop update mantis 25624
    @POST("Shoplist/EditModifiedShop")
    fun getModifiedShopListApi(@Body addQuestion: ShopModifiedUpdateList?): Observable<BaseResponse>

    @POST("RubyFoodLead/QuestionListEdit")
    fun getAddQuestionUpdateSubmit(@Body addQuestion:AddQuestionSubmitRequestData?): Observable<BaseResponse>

    //02-11-2021
    @FormUrlEncoded
    @POST("DuplicateRecords/PhoneNo")
    fun getDuplicationshopPhoneNumber(@Field("user_id") user_id: String,@Field("session_token") session_token: String,@Field("new_shop_phone") new_shop_phone: String):
            Observable<BaseResponse>

    @POST("Shoplist/AddShop")
    fun getAddShop(@Body addShop: AddShopRequestData?): Observable<AddShopResponse>

    // 2.0 NearByShopsListFragment AppV 4.0.6   Contact Multi Api called Add & Update
    @POST("ShopMultipleContactMap/AddShopMultiContact")
    fun getMutiContact(@Body multiContact: multiContactRequestData?): Observable<BaseResponse>

    @POST("ShopMultipleContactMap/EditShopMultiContact")
    fun updateMutiContact(@Body multiContact: multiContactRequestData?): Observable<BaseResponse>
    @FormUrlEncoded
    @POST("ShopMultipleContactMap/FetchShopMultiContact")
    fun fetchMultiContactData(@Field("user_id") user_id: String,@Field("session_token") session_token: String): Observable<ShopListSubmitResponse>


    @FormUrlEncoded
    @POST("Shoplist/ShopAttachmentImagesList")
    fun geimagelist(@Field("shop_id") shop_id: String,@Field("user_id") user_id: String,@Field("session_token") session_token: String): Observable<imageListResponse>

    @Multipart
    @POST("ShopRegistration/NewShopRegister")
    fun getAddShopWithDocImage(@Query("data") addShop: String, @Part logo_img_data: MultipartBody.Part?): Observable<AddShopResponse>

    @Multipart
    @POST("ShopRegistration/AddCompetitorImage")
    fun getAddShopCompetetorImage(@Query("data") addShop: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>

    /*9-12-2021*/
    @Multipart
    @POST("RubyLeadImage/RubyLeadImage1Save")
    fun getAddShopUploadImage(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>
    @Multipart
    @POST("RubyLeadImage/RubyLeadImage2Save")
    fun getAddShopUploadImage2(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>
    /*9-12-2021*/

    /*Mutliple Image*/
    @Multipart
    @POST("ShopRegistration/ShopAttachmentImage1")
    fun UploadAttachImage1(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>

    @Multipart
    @POST("ShopRegistration/ShopAttachmentImage2")
    fun UploadAttachImage2(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>

    @Multipart
    @POST("ShopRegistration/ShopAttachmentImage3")
    fun UploadAttachImage3(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>

    @Multipart
    @POST("ShopRegistration/ShopAttachmentImage4")
    fun UploadAttachImage4(@Query("data") addImageupload: String, @Part competitor_img: MultipartBody.Part?): Observable<BaseResponse>

    @Multipart
    @POST("ShopRegistration/RegisterShop")
    fun getAddShopWithImage(@Query("data") addShop: String, @Part logo_img_data: MultipartBody.Part?): Observable<AddShopResponse>

    @Multipart
    @POST("ShopRegistration/RegisterShop")
    fun getAddShopWithoutImage(@Query("data") addShop: String): Observable<AddShopResponse>


    @Multipart
    @POST("MultipartFile/upload")
    fun uploadImage(@Part logo_img_data: MultipartBody.Part?): Observable<BaseResponse>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): AddShopApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                    .build()

            return retrofit.create(AddShopApi::class.java)
        }

        fun createWithoutMultipart(): AddShopApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(AddShopApi::class.java)
        }
    }
}