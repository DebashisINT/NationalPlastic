package com.breezenationalplasticfsm.features.shopdetail.presentation.api

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.addshop.model.AddLogReqData
import com.breezenationalplasticfsm.features.addshop.model.AddShopRequestData
import com.breezenationalplasticfsm.features.addshop.model.AddShopResponse
import com.breezenationalplasticfsm.features.addshop.model.LogFileResponse
import com.breezenationalplasticfsm.features.addshop.model.UpdateAddrReq
import com.breezenationalplasticfsm.features.addshop.model.UpdateAddressShop
import com.breezenationalplasticfsm.features.contacts.AutoMailDtls
import com.breezenationalplasticfsm.features.contacts.CallHisDtls
import com.breezenationalplasticfsm.features.contacts.CompanyReqData
import com.breezenationalplasticfsm.features.contacts.ContactMasterRes
import com.breezenationalplasticfsm.features.contacts.SourceMasterRes
import com.breezenationalplasticfsm.features.contacts.StageMasterRes
import com.breezenationalplasticfsm.features.contacts.StatusMasterRes
import com.breezenationalplasticfsm.features.contacts.TypeMasterRes
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.document.model.AddEditDocumentInputParams
import com.breezenationalplasticfsm.features.document.model.DocumentAttachmentModel
import com.breezenationalplasticfsm.features.login.model.WhatsappApiData
import com.breezenationalplasticfsm.features.login.model.WhatsappApiFetchData
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Saikat on 10-10-2018.
 */
class EditShopRepo(val apiService: EditShopApi) {

    fun editShop(shop: AddShopRequestData): Observable<AddShopResponse> {
        return apiService.editShop(shop)
    }

    fun whatsAppStatusSync(obj: WhatsappApiData): Observable<BaseResponse> {
        return apiService.whatsAppStatusSyncApi(obj)
    }

    fun whatsAppStatusFetch(user_id: String): Observable<WhatsappApiFetchData> {
        return apiService.whatsAppStatusFetchApi(user_id)
    }

    fun callCompanyMaster(session_token: String): Observable<ContactMasterRes> {
        return apiService.callCompanyMasterApi(session_token)
    }

    fun saveCompanyMaster(companyName: String): Observable<BaseResponse> {
        return apiService.saveCompanyMasterApi(Pref.session_token.toString(),Pref.user_id.toString(),companyName)
    }

    fun saveCompanyMasterNw(companyName: CompanyReqData): Observable<BaseResponse> {
        return apiService.saveCompanyMasterApiNw(companyName)
    }

    fun callTypeMaster(session_token: String): Observable<TypeMasterRes> {
        return apiService.callTypeMasterApi(session_token)
    }

    fun callStatusMaster(session_token: String): Observable<StatusMasterRes> {
        return apiService.callStatusMasterApi(session_token)
    }
    fun callSourceMaster(session_token: String): Observable<SourceMasterRes> {
        return apiService.callSourceMasterApi(session_token)
    }

    fun callStageMaster(session_token: String): Observable<StageMasterRes> {
        return apiService.callStageMasterApi(session_token)
    }

    fun callLogListSaveApi(callLogHisSave: CallHisDtls?): Observable<BaseResponse> {
        return apiService.callLogListSaveApi(callLogHisSave)
    }

    fun callUpdateAdressShopSaveApi(updateAddrReq: UpdateAddrReq?): Observable<BaseResponse> {
        return apiService.callUpdateAddressSaveApi(updateAddrReq)
    }

    fun callCallListHisAPI(user_id: String): Observable<CallHisDtls> {
        return apiService.callCallListHisAPI(user_id)
    }

    fun autoMailDtls(user_id: String): Observable<AutoMailDtls> {
        return apiService.autoMailDtlsAPI(user_id)
    }

    fun addShopWithImage(shop: AddShopRequestData, shop_image: String?, context: Context): Observable<AddShopResponse> {
        var profile_img_data: MultipartBody.Part? = null
        var profile_img_file: File? = null

        if (!TextUtils.isEmpty(shop_image))
            profile_img_file = FileUtils.getFile(context, Uri.parse(shop_image))

        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("shop_image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File
            mFile = (context as DashboardActivity).getShopDummyImageFile()
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
        return apiService.editShopWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun addShopWithImage(shop: AddShopRequestData, shop_image: String?, degree_image: String?, context: Context): Observable<AddShopResponse> {
        var profile_img_data: MultipartBody.Part? = null
        var degree_img_data: MultipartBody.Part? = null

        /*val profile_img_file = FileUtils.getFile(context, Uri.parse(shop_image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("shop_image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("shop_image", mFile.name, profileImgBody)
        }

        if (!TextUtils.isEmpty(degree_image)) {
            val degree_image_file = FileUtils.getFile(context, Uri.parse(degree_image))
            if (degree_image_file != null && degree_image_file.exists()) {
                val degreeImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), degree_image_file)
                degree_img_data = MultipartBody.Part.createFormData("degree_image", degree_image_file.name, degreeImgBody)
            } else {
                var mFile: File
                mFile = (context as DashboardActivity).getShopDummyImageFile()
                val degreeImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
                degree_img_data = MultipartBody.Part.createFormData("degree_image", mFile.name, degreeImgBody)
            }
        }*/

        if (!TextUtils.isEmpty(shop_image)) {
            val profile_img_file = FileUtils.getFile(context, Uri.parse(shop_image))
            if (profile_img_file != null && profile_img_file.exists()) {
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
                profile_img_data = MultipartBody.Part.createFormData("shop_image", profile_img_file.name, profileImgBody)
            } else {
                var mFile: File
                mFile = (context as DashboardActivity).getShopDummyImageFile()
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
                profile_img_data = MultipartBody.Part.createFormData("shop_image", mFile.name, profileImgBody)
            }
        } else if (!TextUtils.isEmpty(degree_image)) {
            val degree_image_file = FileUtils.getFile(context, Uri.parse(degree_image))
            if (degree_image_file != null && degree_image_file.exists()) {
                val degreeImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), degree_image_file)
                degree_img_data = MultipartBody.Part.createFormData("degree", degree_image_file.name, degreeImgBody)
            } else {
                var mFile: File
                mFile = (context as DashboardActivity).getShopDummyImageFile()
                val degreeImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
                degree_img_data = MultipartBody.Part.createFormData("degree", mFile.name, degreeImgBody)
            }
        }

        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = Gson().toJson(shop)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return if (degree_img_data != null)
            apiService.editShopWithDegImage(jsonInString, degree_img_data)
        else
            apiService.editShopWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }



    fun addLogfile(user_id: AddLogReqData, shop_image: String, context: Context): Observable<LogFileResponse> {
       /* var log_attachments:MultipartBody.Part? = null
        if (!TextUtils.isEmpty(shop_image)) {
            val profile_img_file = FileUtils.getFile(context, Uri.parse(shop_image))
            if (profile_img_file != null && profile_img_file.exists()) {
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
                log_attachments = MultipartBody.Part.createFormData("attachments", profile_img_file.name, profileImgBody)
            }
        }

        val imageName = Pref.user_id + "~" + shop_image+ "~"
        //val fileName = imageName + "_" + System.currentTimeMillis() + "." + fileExt
        val fileName = imageName + "_" + System.currentTimeMillis() + ".txt"

        Log.e("Document Image", "File Name=========> $fileName")
        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = Gson().toJson(user_id)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }


        return  apiService.logshareFile(jsonInString, log_attachments)
        // return apiService.getAddShopWithoutImage(jsonInString)*/



        //////
        var log_attachments_new:MultipartBody.Part? = null
        var log_attachments_file: File? = null
        log_attachments_file = File(shop_image)
        val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), log_attachments_file)
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(user_id)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        log_attachments_new = MultipartBody.Part.createFormData("attachments", "${Pref.user_id}.zip", profileImgBody)
        return  apiService.logshareFile(jsonInString, log_attachments_new)
    }




}