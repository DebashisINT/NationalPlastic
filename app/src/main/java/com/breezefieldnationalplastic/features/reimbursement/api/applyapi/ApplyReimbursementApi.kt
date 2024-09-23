package com.breezefieldnationalplastic.features.reimbursement.api.applyapi

import com.breezefieldnationalplastic.app.NetworkConstant
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.features.reimbursement.model.AppliedReimbursementDeleteInputModel
import com.breezefieldnationalplastic.features.reimbursement.model.ApplyReimbursementInputModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Saikat on 25-01-2019.
 */
interface ApplyReimbursementApi {

    @POST("BillsUpload/Apply")
    fun applyReimbursement(@Body applyReimburseMentInput: ApplyReimbursementInputModel): Observable<BaseResponse>

    @POST("BillsUpload/DeleteReimbersment")
    fun deleteReimbursement(@Body appliedReimbursementDeleteInput: AppliedReimbursementDeleteInputModel): Observable<BaseResponse>

    @Multipart
    @POST("BillsUpload/POST")
    fun applyReimbursementImage(@Part bills: List<MultipartBody.Part?>): Observable<BaseResponse>

    companion object Factory {
        fun create(): ApplyReimbursementApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(ApplyReimbursementApi::class.java)
        }
    }
}