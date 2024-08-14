package com.breezenationalplasticfsm.features.dashboard.presentation.api

import android.content.Context
import android.net.Uri
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.dashboard.presentation.model.AudioSyncModel
import com.breezenationalplasticfsm.features.dashboard.presentation.model.ShopVisitImageUploadInputModel
import com.breezenationalplasticfsm.features.location.LocationFuzedService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Saikat on 28-09-2018.
 */
class ShopVisitImageUploadRepo(val apiService: ShopVisitImageUploadApi) {

    fun visitShopWithImage(shop: ShopVisitImageUploadInputModel, shop_image: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = /*File(shop_image)*/ FileUtils.getFile(context, Uri.parse(shop_image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("shop_image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("shop_image", mFile.name, profileImgBody)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(shop)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.visitShopWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun visitShopWithAudio(shop: ShopVisitImageUploadInputModel, audio_link: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = File(audio_link) //FileUtils.getFile(context, Uri.parse(audio_link))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("audio", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("audio", mFile.name, profileImgBody)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(shop)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.visitShopWithAudio(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun syncNewShopAudio(obj: AudioSyncModel, audio_link: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null
        val profile_img_file = File(audio_link) //FileUtils.getFile(context, Uri.parse(audio_link))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("audio", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("audio", mFile.name, profileImgBody)
        }

        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(obj)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.syncNewShopAudioApi(jsonInString, profile_img_data)
    }

    fun syncShopNewAudio(shop: ShopVisitImageUploadInputModel, audio_link: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null
        val profile_img_file = File(audio_link) //FileUtils.getFile(context, Uri.parse(audio_link))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("audio", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("audio", mFile.name, profileImgBody)
        }
        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(shop)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.syncShopNewAudioApi(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

}