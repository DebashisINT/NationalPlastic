package com.breezenationalplasticfsm.features.photoReg.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.addshop.model.AddShopRequestCompetetorImg
import com.breezenationalplasticfsm.features.damageProduct.model.AddBreakageReqData
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.login.model.userconfig.UserConfigResponseModel
import com.breezenationalplasticfsm.features.myjobs.model.WIPImageSubmit
import com.breezenationalplasticfsm.features.photoReg.model.*
import com.breezenationalplasticfsm.features.stockAddCurrentStock.api.ShopAddStockApi
import com.breezenationalplasticfsm.features.stockAddCurrentStock.model.CurrentStockGetData
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GetUserListPhotoRegRepository(val apiService : GetUserListPhotoRegApi) {

    fun getAllAadhaar(session_token: String): Observable<GetAllAadhaarResponse> {
        return apiService.getAllAadhaar(session_token)
    }

    fun getUserListApi(user_id: String, session_token: String): Observable<GetUserListResponse> {
        return apiService.getUserListApi(user_id,session_token)
    }

    fun deleteUserPicApi(user_id: String, session_token: String): Observable<DeleteUserPicResponse> {
        return apiService.deleteUserPicApi(user_id,session_token)
    }

    fun getUserManualPhotoStatus(user_id: String, session_token: String): Observable<UserConfigResponseModel> {
        return apiService.getUserManualPhotoStatusAPI(user_id,session_token)
    }


    fun getUserFacePicUrlApi(user_id: String, session_token: String): Observable<UserFacePicUrlResponse> {
        return apiService.getUserFacePic(user_id,session_token)
    }

    fun sendUserAadhaarApi(aadhaarSubmitData:AadhaarSubmitData): Observable<BaseResponse> {
        return apiService.submitAadhaarDetailsSingle(aadhaarSubmitData)
    }

    fun updateUserType(user_id: String, session_token: String,type_id:String): Observable<BaseResponse> {
        return apiService.updateUserTypeApi(user_id,session_token,type_id)
    }

    fun addEmpPhone(user_id: String, session_token: String,usrContID:String,phoneNo:String): Observable<BaseResponse> {
        return apiService.addEmpPhoneApi(user_id,session_token,usrContID,phoneNo)
    }

    fun updateEmpPhone(user_id: String, session_token: String,usrContID:String,oldPhoneNo:String,newPhoneNo:String): Observable<BaseResponse> {
        return apiService.updateEmpPhoneApi(user_id,session_token,usrContID,oldPhoneNo,newPhoneNo)
    }

    fun updateOtherID(update_other_id_user_contactid: String, other_id: String): Observable<BaseResponse> {
        return apiService.updateOtherIDApi(update_other_id_user_contactid,other_id)
    }

    fun updateUserLoginID(update_other_id_user_contactid: String, other_id: String): Observable<BaseResponse> {
        return apiService.updateUserLoginIDApi(update_other_id_user_contactid,other_id)
    }


    fun sendUserAadhaarInfoNewApi(aadhaarSubmitData:AadhaarSubmitDataNew): Observable<BaseResponse> {
        return apiService.submitAadhaarDetailsNewSingle(aadhaarSubmitData)
    }
    fun sendUserAadhaarInfoNewApi(user_id:String ,name_on_aadhaar:String,DOB_on_aadhaar:String,Aadhaar_number:String,REG_DOC_TYP:String): Observable<BaseResponse> {
        return apiService.submitAadhaarDetailsNewSingleWithDtls(user_id,name_on_aadhaar,DOB_on_aadhaar,Aadhaar_number,REG_DOC_TYP)
    }

    fun updateUserName(obj:UpdateUserNameModel): Observable<UpdateUserNameResponse> {
        return apiService.updateUserNameApi(obj)
    }

    fun addUserFaceRegImg(obj: UserPhotoRegModel, user_image: String?, context: Context,user_contactid:String?): Observable<FaceRegResponse> {
        var profile_img_data: MultipartBody.Part? = null
        if (!TextUtils.isEmpty(user_image)){
            val profile_img_file = FileUtils.getFile(context, Uri.parse(user_image))
            if (profile_img_file != null && profile_img_file.exists()) {
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
                profile_img_data = MultipartBody.Part.createFormData("attachments", profile_img_file.name.replaceAfter("cropped",user_contactid.toString()).replace("cropped","")+".jpg", profileImgBody)
            }
        }


        var jsonInString = ""
        try {
            jsonInString = Gson().toJson(obj)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return  apiService.getAddUserFaceImage(jsonInString, profile_img_data)
    }

    fun addUserAadhaarImg(obj: UserPhotoRegModel, user_image: String?, context: Context,user_contactid:String?): Observable<AadhaarPicRegResponse> {
        var profile_img_data: MultipartBody.Part? = null
        if (!TextUtils.isEmpty(user_image)){
            val profile_img_file = FileUtils.getFile(context, Uri.parse(user_image))
            if (profile_img_file != null && profile_img_file.exists()) {
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
                profile_img_data = MultipartBody.Part.createFormData("attachments", profile_img_file.name.replaceAfter("cropped",user_contactid.toString()).replace("cropped","")+".jpg", profileImgBody)
            }
        }


        var jsonInString = ""
        try {
            jsonInString = Gson().toJson(obj)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return  apiService.getAddUserAadhaarImage(jsonInString, profile_img_data)
    }

    fun addImgwithdata(obj: AddBreakageReqData, user_image: String?, context: Context, user_contactid:String?): Observable<ImageResponse> {
        var profile_img_data: MultipartBody.Part? = null
        if (!TextUtils.isEmpty(user_image)){
            //val profile_img_file = FileUtils.getFile(context, Uri.parse(user_image))
            val profile_img_file = File(user_image)
            if (profile_img_file != null && profile_img_file.exists()) {
                val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
                profile_img_data = MultipartBody.Part.createFormData("attachments", profile_img_file.name.replaceAfter("cropped",user_contactid.toString()).replace("cropped","")+".jpg", profileImgBody)
            }
        }


        var jsonInString = ""
        try {
            jsonInString = Gson().toJson(obj)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return  apiService.subAddImage(jsonInString, profile_img_data)
    }


    fun submitAadhaarDetails(aadhaarSubmitData: AadhaarSubmitData, wipImageSubmitList: ArrayList<WIPImageSubmit>, context: Context?): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null
        val multiPartArray: java.util.ArrayList<MultipartBody.Part> = arrayListOf()

        for (item in wipImageSubmitList) {
            Thread.sleep(500)

            var attachment: File? = null
            if (item.link.startsWith("file"))
                attachment = FileUtils.getFile(context, Uri.parse(item.link))
            else {
                attachment = File(item.link)

                if (!attachment?.exists()!!) {
                    attachment?.createNewFile()
                }
            }

            val attachmentBody = RequestBody.create(MediaType.parse("multipart/form-data"), attachment)
            //val fileExt = FileUtils.getFile(context, Uri.parse(item.link)).extension //File(item.link).extension

            var fileExt = ""
            fileExt = if (item.link.startsWith("file"))
                FileUtils.getFile(context, Uri.parse(item.link)).extension
            else
                File(item.link).extension

            val imageName = aadhaarSubmitData.aadhaar_holder_user_id + aadhaarSubmitData.date
            val fileName = imageName + item.type + "_" + System.currentTimeMillis() + "." + fileExt

            Log.e("Work Reschedule Image", "File Name=========> $fileName")

            profile_img_data = MultipartBody.Part.createFormData("attachments", fileName, attachmentBody)
            multiPartArray.add(profile_img_data)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(aadhaarSubmitData)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return apiService.submitAadhaarDetails(jsonInString, multiPartArray)
    }


}