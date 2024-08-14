package com.breezenationalplasticfsm.features.lead.api

import com.breezenationalplasticfsm.app.NetworkConstant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.lead.model.*
import com.breezenationalplasticfsm.features.taskManagement.AddTaskReq
import com.breezenationalplasticfsm.features.taskManagement.EditTaskReq
import com.breezenationalplasticfsm.features.taskManagement.TaskViewRes
import com.breezenationalplasticfsm.features.taskManagement.model.TaskListReq
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded


interface GetLeadListApi {

    @POST("LeadEnquiryDetails/LeadEnquiryWiseCustList")
    fun getCustomerList(@Body getCustomerList: CustomerListReq?): Observable<CustomerLeadResponse>

    @POST("LeadEnquiryDetails/SaveActivity")
    fun submitActivityListAPI(@Body list: AddActivityReq?): Observable<BaseResponse>

    @POST("LeadEnquiryDetails/UpdateActivity")
    fun editActivityAPI(@Body data: EditActivityReq?): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("LeadEnquiryDetails/ShowActivityList")
    fun viewActivityList(@Field("crm_id") crm_id: String): Observable<ActivityViewRes>


    @POST("Task/TaskPriorityWiseList")
    fun getTaskList(@Body getTaskList: TaskListReq?): Observable<TaskResponse>

    @POST("Task/AddTaskDetailList")
    fun submitTaskListAPI(@Body list: AddTaskReq?): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("Task/GetTaskDetailList")
    fun viewTaskList(@Field("task_id") crm_id: String): Observable<TaskViewRes>

    @POST("Task/EditTaskDetailList")
    fun editTaskAPI(@Body data: EditTaskReq?): Observable<BaseResponse>



    companion object Factory {
        fun create(): GetLeadListApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOutNoRetry())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(GetLeadListApi::class.java)
        }
    }
}