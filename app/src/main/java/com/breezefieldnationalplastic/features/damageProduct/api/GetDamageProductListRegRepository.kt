package com.breezefieldnationalplastic.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.breezefieldnationalplastic.app.FileUtils
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.NewQuotation.model.*
import com.breezefieldnationalplastic.features.addshop.model.AddShopRequestData
import com.breezefieldnationalplastic.features.addshop.model.AddShopResponse
import com.breezefieldnationalplastic.features.damageProduct.model.DamageProductResponseModel
import com.breezefieldnationalplastic.features.damageProduct.model.delBreakageReq
import com.breezefieldnationalplastic.features.damageProduct.model.viewAllBreakageReq
import com.breezefieldnationalplastic.features.login.model.userconfig.UserConfigResponseModel
import com.breezefieldnationalplastic.features.myjobs.model.WIPImageSubmit
import com.breezefieldnationalplastic.features.photoReg.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GetDamageProductListRegRepository(val apiService : GetDamageProductListApi) {

    fun viewBreakage(req: viewAllBreakageReq): Observable<DamageProductResponseModel> {
        return apiService.viewBreakage(req)
    }

    fun delBreakage(req: delBreakageReq): Observable<BaseResponse>{
        return apiService.BreakageDel(req.user_id!!,req.breakage_number!!,req.session_token!!)
    }

}