package com.breezenationalplasticfsm.features.photoReg.api

import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.login.model.userconfig.UserConfigResponseModel
import com.breezenationalplasticfsm.features.photoReg.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GetUserListPhotoRegApi {

    @FormUrlEncoded
    @POST("UserWiseAadharInfo/UserAadharList")
    fun getAllAadhaar(@Field("session_token") session_token: String):
            Observable<GetAllAadhaarResponse>

    @FormUrlEncoded
    @POST("FaceRegistration/UserList")
    fun getUserListApi(@Field("user_id") user_id: String,@Field("session_token") session_token: String):
            Observable<GetUserListResponse>

    @Multipart
    @POST("FaceImageDetection/FaceImage")
    fun getAddUserFaceImage(@Query("data") face: String, @Part attachments: MultipartBody.Part?): Observable<FaceRegResponse>


    @Multipart
    @POST("BreakageMaterialsDetectionInfo/BreakageMaterialsSave")
    fun subAddImage(@Query("data") face: String, @Part attachments: MultipartBody.Part?): Observable<ImageResponse>

    @FormUrlEncoded
    @POST("FaceRegistration/FaceMatch")
    fun getUserFacePic(@Field("user_id") user_id: String,@Field("session_token") session_token: String ): Observable<UserFacePicUrlResponse>


    @Multipart
    @POST("AadharImageDetection/AadharImageSave")
    fun getAddUserAadhaarImage(@Query("data") face: String, @Part attachments: MultipartBody.Part?): Observable<AadhaarPicRegResponse>

    @FormUrlEncoded
    @POST("FaceImageDetection/FaceImgDelete")
    fun deleteUserPicApi(@Field("user_id") user_id: String,@Field("session_token") session_token: String):
            Observable<DeleteUserPicResponse>

    @FormUrlEncoded
    @POST("Configuration/Userwise")
    fun getUserManualPhotoStatusAPI(@Field("user_id") user_id: String,@Field("session_token") session_token: String):
            Observable<UserConfigResponseModel>

    @FormUrlEncoded
    @POST("FaceRegistration/FaceRegTypeIDSave")
    fun updateUserTypeApi(@Field("user_id") user_id: String,@Field("session_token") session_token: String,@Field("type_id") type_id: String): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("PhoneNo/InsertPhoneNo")
    fun addEmpPhoneApi(@Field("user_id") user_id: String,@Field("session_token") session_token: String,@Field("user_contactid") user_contactid: String,
                       @Field("phone_no") phone_no: String):
            Observable<BaseResponse>

    @FormUrlEncoded
    @POST("PhoneNo/UpdatePhoneNo")
    fun updateEmpPhoneApi(@Field("user_id") user_id: String,@Field("session_token") session_token: String,@Field("user_contactid") user_contactid: String,
                          @Field("old_phone_no") old_phone_no: String,@Field("new_phone_no") new_phone_no: String):
            Observable<BaseResponse>

    @FormUrlEncoded
    @POST("UpdateUserInformations/UpdateUserOtherID")
    fun updateOtherIDApi(@Field("update_other_id_user_contactid") update_other_id_user_contactid: String,
                         @Field("other_id") other_id: String): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("UpdateUserInformations/UpdateUserLoginID")
    fun updateUserLoginIDApi(@Field("update_login_id_of_user_id") update_login_id_of_user_id: String,
                             @Field("user_login_id_new") user_login_id_new: String): Observable<BaseResponse>

    @POST("AadharImageDetectionInfo/UserAadharInfoSave")
    fun submitAadhaarDetailsNewSingle(@Body aadhaarSubmitData: AadhaarSubmitDataNew): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("AadharImageDetectionInfo/UserAadharInfoSave")
    fun submitAadhaarDetailsNewSingleWithDtls(@Field("user_id") user_id: String,
                                              @Field("name_on_aadhaar") name_on_aadhaar: String,@Field("DOB_on_aadhaar") DOB_on_aadhaar: String,
                                              @Field("Aadhaar_number") Aadhaar_number: String,@Field("REG_DOC_TYP") REG_DOC_TYP: String): Observable<BaseResponse>


    @POST("UpdateUserInformations/UpdateUserName")
    fun updateUserNameApi(@Body obj: UpdateUserNameModel):
            Observable<UpdateUserNameResponse>

    @Multipart
    @POST("UserAadharImageSave/UserAadharImage")
    fun submitAadhaarDetails(@Query("data") data: String, @Part attachments: List<MultipartBody.Part?>): Observable<BaseResponse>

    @POST("UserWiseAadharInfo/UserAadharInfo")
    fun submitAadhaarDetailsSingle(@Body aadhaarSubmitData: AadhaarSubmitData): Observable<BaseResponse>

    companion object Factory {
        fun create(): GetUserListPhotoRegApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOutNoRetry())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.BASE_URL)
                .build()

            return retrofit.create(GetUserListPhotoRegApi::class.java)
        }




        fun createFacePic(): GetUserListPhotoRegApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                .build()

            return retrofit.create(GetUserListPhotoRegApi::class.java)
        }

        fun submitPic(): GetUserListPhotoRegApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                .build()

            return retrofit.create(GetUserListPhotoRegApi::class.java)
        }


        fun createMultiPart(): GetUserListPhotoRegApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                .build()

            return retrofit.create(GetUserListPhotoRegApi::class.java)
        }
    }


}