package com.nationalplasticfsm.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.nationalplasticfsm.app.FileUtils
import com.nationalplasticfsm.base.BaseResponse
import com.nationalplasticfsm.features.NewQuotation.model.*
import com.nationalplasticfsm.features.addshop.model.AddShopRequestData
import com.nationalplasticfsm.features.addshop.model.AddShopResponse
import com.nationalplasticfsm.features.damageProduct.model.DamageProductResponseModel
import com.nationalplasticfsm.features.damageProduct.model.delBreakageReq
import com.nationalplasticfsm.features.damageProduct.model.viewAllBreakageReq
import com.nationalplasticfsm.features.login.model.userconfig.UserConfigResponseModel
import com.nationalplasticfsm.features.myjobs.model.WIPImageSubmit
import com.nationalplasticfsm.features.photoReg.model.*
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