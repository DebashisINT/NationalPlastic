package com.breezenationalplasticfsm.features.timesheet.api

import android.content.Context
import com.breezenationalplasticfsm.app.Pref
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import com.breezenationalplasticfsm.features.timesheet.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

/**
 * Created by Saikat on 29-Apr-20.
 */
class TimeSheetRepo(val apiService: TimeSheetApi) {
    fun timeSheetList(date: String): Observable<TimeSheetListResponseModel> {

        return apiService.getTimeSheetList(Pref.session_token!!, Pref.user_id!!, date)
    }

    fun deleteTimeSheet(id: String): Observable<EditDeleteTimesheetResposneModel> {
        Timber.d("timesheet_api TimeSheet/DeleteTimeSheet")
        return apiService.deleteTimeSheet(Pref.session_token!!, Pref.user_id!!, id)
    }

    fun timeSheetConfig(isAdd: Boolean): Observable<TimeSheetConfigResponseModel> {
        Timber.d("timesheet_api TimeSheet/GetTimeSheetConfig")
        return apiService.timeSheetConfig(Pref.session_token!!, Pref.user_id!!, isAdd)
    }

    fun getTimeSheetDropdown(): Observable<TimeSheetDropDownResponseModel> {
        Timber.d("timesheet_api TimeSheet/GetDropDown")
        return apiService.getTimeSheetDropdownData(Pref.session_token!!, Pref.user_id!!)
    }

    fun addTimeSheet(addTimeSheet: AddTimeSheetInputModel): Observable<BaseResponse> {
        Timber.d("timesheet_api TimeSheet/SaveTimeSheet")
        return apiService.addTimesheet(addTimeSheet)
    }

    fun editTimeSheet(editTimeSheet: EditTimeSheetInputModel): Observable<EditDeleteTimesheetResposneModel> {
        Timber.d("timesheet_api TimeSheet/UpdateTimeSheet")
        return apiService.editTimesheet(editTimeSheet)
    }

    fun addTimesheetWithImage(timesheet: AddTimeSheetInputModel, image: String, context: Context): Observable<BaseResponse> {
        Timber.d("timesheet_api TimeSheetImage/SaveTimeSheet")
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file =  File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(timesheet)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.addTimesheetWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun editTimesheetWithImage(timesheet: EditTimeSheetInputModel, image: String, context: Context): Observable<EditDeleteTimesheetResposneModel> {
        Timber.d("timesheet_api TimeSheetImage/UpdateTimeSheet")
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file =  File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file!=null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(timesheet)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.editTimesheetWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }
}