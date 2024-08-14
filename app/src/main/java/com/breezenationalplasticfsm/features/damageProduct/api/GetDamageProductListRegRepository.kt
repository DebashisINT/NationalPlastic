package com.breezenationalplasticfsm.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.breezenationalplasticfsm.app.FileUtils
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.NewQuotation.model.*
import com.breezenationalplasticfsm.features.addshop.model.AddShopRequestData
import com.breezenationalplasticfsm.features.addshop.model.AddShopResponse
import com.breezenationalplasticfsm.features.damageProduct.model.DamageProductResponseModel
import com.breezenationalplasticfsm.features.damageProduct.model.delBreakageReq
import com.breezenationalplasticfsm.features.damageProduct.model.viewAllBreakageReq
import com.breezenationalplasticfsm.features.login.model.userconfig.UserConfigResponseModel
import com.breezenationalplasticfsm.features.myjobs.model.WIPImageSubmit
import com.breezenationalplasticfsm.features.photoReg.model.*
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