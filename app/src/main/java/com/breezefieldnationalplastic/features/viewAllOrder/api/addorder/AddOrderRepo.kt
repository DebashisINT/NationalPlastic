package com.breezefieldnationalplastic.features.viewAllOrder.api.addorder

import android.content.Context
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.breezefieldnationalplastic.features.returnsOrder.ReturnRequest
import com.breezefieldnationalplastic.features.timesheet.model.AddTimeSheetInputModel
import com.breezefieldnationalplastic.features.viewAllOrder.model.AddOrderInputParamsModel
import com.breezefieldnationalplastic.features.viewAllOrder.model.NewOrderSaveApiModel
import com.breezefieldnationalplastic.features.viewAllOrder.model.NewStockSync
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Saikat on 01-10-2018.
 */
class AddOrderRepo(val apiService: AddOrderApi) {
    fun addOrder(sessiontoken: String, user_id: String, shop_id: String, order_id: String, order_amount: String, description: String, collection: String, order_date: String): Observable<BaseResponse> {
        return apiService.addOrder(sessiontoken, user_id, shop_id, order_id, order_amount, description, collection, order_date)
    }

    fun addReturn(ReturnRequest:ReturnRequest): Observable<BaseResponse> {
        return apiService.addReturn(ReturnRequest)
    }

    fun addNewOrder(addOrder: AddOrderInputParamsModel): Observable<BaseResponse> {
        return apiService.addNewOrder(addOrder)
    }

    fun addNewStock(syncObj: NewStockSync): Observable<BaseResponse> {
        return apiService.addNewStock(syncObj)
    }

    fun addNewOrder(addOrder: AddOrderInputParamsModel, image: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file =  File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("signature", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("signature", mFile.name, profileImgBody)
        }


        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(addOrder)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.addNewOrderWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }



    fun addOrderNewOrderScr(addOrder: NewOrderSaveApiModel): Observable<BaseResponse> {
        return apiService.addOrderNewOrderScr(addOrder)
    }

}